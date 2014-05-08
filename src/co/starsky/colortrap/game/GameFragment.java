package co.starsky.colortrap.game;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import co.starsky.colortrap.R;
import co.starsky.colortrap.animator.AnimatorPath;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.model.State;
import co.starsky.colortrap.model.Triplet;
import co.starsky.colortrap.util.AnimationUtil;
import co.starsky.colortrap.util.MessageHelper;
import co.starsky.colortrap.view.FontyTextView;
import co.starsky.colortrap.view.adapter.AnimatedAdapter;
import co.starsky.colortrap.view.adapter.GridAdapter;


/**
 * Activity which starts and manages a new game.
 */
public class GameFragment extends Fragment implements AdapterView.OnItemClickListener, AnimatedAdapter.AnimationListener {
    protected static final String KEY_GAMEBOARD = "gameb";
    private float spacing;

    private GameBoard gameBoard;
    private GridView gridView;
    private GridAdapter adapter;
    private FontyTextView messageView;
    private MessageHelper msgHelper;
    private View resetButton;
    private Mode mode;

    private ImageView playerOnePiece;
    private ImageView playerTwoPiece;
    private View playerOneTile;
    private View playerTwoTile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.board, container, false);

        if (savedInstanceState != null && savedInstanceState.getSerializable(KEY_GAMEBOARD) != null) {
            continueGame((GameBoard) savedInstanceState.getSerializable(KEY_GAMEBOARD));
        }

        v.findViewById(R.id.board_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spacing = getResources().getDimension(R.dimen.spacing);

        messageView = (FontyTextView) v.findViewById(R.id.board_text);
        messageView.setText("");
        gridView = (GridView) v.findViewById(R.id.gridview);
        resetButton = v.findViewById(R.id.board_reset);
        playerOnePiece = (ImageView) v.findViewById(R.id.animation_image);
        playerTwoPiece = (ImageView) v.findViewById(R.id.animation_image2);

        resetButton.setVisibility(View.GONE);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButton.setVisibility(View.GONE);
                resetPieces();
                resetBoard(getActivity(), mode);
                setupGridView(getActivity());
                startGame(getActivity(), mode);
                messageView.setText("");
            }
        });

        setupGridView(getActivity());
        return v;
    }

    private void resetPieces() {
        playerOnePiece.setAlpha(0f);
        playerTwoPiece.setAlpha(0f);
    }

    public void startGame(Context context, Mode mode) {
        this.mode = mode;
        didMakeFirstMove = false;

        //gameBoard = new GameBoard(context, mode);
        if (msgHelper == null) {
            msgHelper = new MessageHelper();
        }

        msgHelper.setMode(context, mode);
    }

    public void continueGame(GameBoard gameBoard) {
        this.mode = gameBoard.getMode();
        this.gameBoard = gameBoard;

        if (msgHelper == null) {
            msgHelper = new MessageHelper();
        }

        msgHelper.setMode(getActivity(), mode);
    }

    private void setupGridView(Context context) {
        adapter = new GridAdapter(context, gameBoard);
        adapter.setAnimationListener(this);

        gridView.setOnItemClickListener(this);
        gridView.setAdapter(adapter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.animate();
            }
        }, 100);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("allie", "" + position + " / " + view.getX());
        step(view, position);
    }

    private boolean didMakeFirstMove = false;
    private void step(final View view, final int position) {
        State currentState = gameBoard.getCurrentState();
        switch (currentState) {
            case PLACE_PIECE:
                boolean success = gameBoard.setupPlayer(position);
                if (success) {
                    final View piece;

                    if (!didMakeFirstMove) {
                        piece = playerOnePiece;
                        playerOneTile = view;
                    } else {
                        piece = playerTwoPiece;
                        playerTwoTile = view;
                    }
                    showPiece(piece, view);
                    didMakeFirstMove = true;
                }
                displayMessage();
                break;
            case TURN_PLAYER:
                takeTurn(view, position);
                break;
            case GAME_OVER:
                handleGameOver();
                break;
            default: break;
        }
    }

    private void takeTurn(final View view, final int position) {
        Triplet<Integer, Integer, Integer> t = gameBoard.takeTurn(position);

        if (t == null) {
            displayInvalidMessage();
        } else if (gameBoard.getCurrentState() == State.GAME_OVER) {
            handleGameOver();
            animateTurn(t);
        } else {
            displayMessage();
            animateTurn(t);
            if (t.getA() == 0) {
                playerOneTile = view;
            } else {
                playerTwoTile = view;
            }
        }
    }

    private void showPiece(final View piece, final View tile) {
        final float x = tile.getX() + ((tile.getWidth() / 2) - (piece.getWidth() / 2));
        final float y = tile.getY() + ((tile.getHeight() / 2) - (piece.getHeight() / 2));

        piece.setX(x);
        piece.setY(y);
        piece.setAlpha(1);
        piece.bringToFront();
    }

    /** Animates a player piece to its new grid tile. */
    private void animateTurn(final Triplet<Integer, Integer, Integer> t) {
        final View piece;
        final View tile;

        if (t.getA() == 0) {
            piece = playerOnePiece;
            tile = playerOneTile;
        } else {
            piece = playerTwoPiece;
            tile = playerTwoTile;
        }

        piece.bringToFront();

        // Math! Gathers measurements for start to end location and curve offsets.

        // Grid View indices
        final float oldPos = (float) t.getB();
        final float newPos = (float) t.getC();

        // Screen start location
        final float x = tile.getX() + ((tile.getWidth() / 2) - (piece.getWidth() / 2));
        final float y = tile.getY() + ((tile.getHeight() / 2) - (piece.getHeight() / 2));

        // Tile size including grid padding
        final float oneTileX = tile.getWidth() + spacing;
        final float oneTileY = tile.getHeight() + spacing;

        final float n = (newPos - oldPos) / GameBoard.COL_NUM; // overall move difference
        final float vertical = (int) n; // difference in # of columns

        final float xOffset = oneTileX * ((n - vertical) * GameBoard.COL_NUM); // second val is difference in # of rows
        final float yOffset = oneTileY * vertical;

        float xCurve = AnimationUtil.getCurve(oneTileX, xOffset);
        float yCurve = AnimationUtil.getCurve(oneTileY, yOffset);

        if (n < 0) { // Left / Up movements have an inverse curve.
            xCurve = -xCurve;
            yCurve = -yCurve;
        }

        final int duration = AnimationUtil.getCurveDuration(xOffset, yOffset);
        final AnimatorPath path = AnimationUtil.getCurvedPath(x, y, xOffset, yOffset, xCurve, yCurve);
        AnimationUtil.startPathAnimation(piece, path, duration, new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void displayInvalidMessage() {
        displayMessage(msgHelper.getInvalidMessage());
    }

    private void displayMessage() {
        displayMessage(gameBoard.getCurrentState());
    }

    /** Display some message to the user based on the given State. */
    private void displayMessage(State state) {
        String message = "";
        switch (mode) {
            case HOTSEAT:
                message = msgHelper.getMessage(state, gameBoard.getCurrentPlayerName());
                break;
            case COMPUTER:
                message = msgHelper.getMessage(state);
                break;
        }
        displayMessage(message);
    }

    private void displayMessage(final String message) {
        AnimationUtil.fadeOutView(messageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageView.setText(message);
                AnimationUtil.fadeInView(messageView);
            }
        }, AnimationUtil.DEFAULT_FADE_DURATION);
    }

    private void handleGameOver() {
        resetButton.setVisibility(View.VISIBLE);
        messageView.setText(msgHelper.getGameOverMessage(gameBoard.getCurrentPlayerName()));
    }

    @Override
    public void onAnimationComplete() {
        displayMessage(State.PLACE_PIECE);
    }

    protected GameBoard getGameBoard() {
        return gameBoard;
    }

    public void resetBoard(Context c, Mode m) {
        gameBoard = new GameBoard(c, m);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        storeData(outState);
    }

    /** Stores session Objects in the given Bundle. */
    private void storeData(Bundle bundle) {
        if (bundle == null) {
            return;
        }

        if (gameBoard != null && gameBoard.getCurrentState() != State.GAME_OVER) {
            bundle.putSerializable(KEY_GAMEBOARD, gameBoard);
        }
    }

}
