package co.starsky.colortrap.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import co.starsky.colortrap.R;
import co.starsky.colortrap.animator.AnimatorPath;
import co.starsky.colortrap.model.GameBoard;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.model.State;
import co.starsky.colortrap.model.Triplet;
import co.starsky.colortrap.util.AnimationUtil;
import co.starsky.colortrap.util.MessageHelper;
import co.starsky.colortrap.view.AnimatedAdapter;
import co.starsky.colortrap.view.FontyTextView;
import co.starsky.colortrap.view.GridAdapter;


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
    private ImageView playerOneView;
    private ImageView playerTwoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.board, container, false);

        if (savedInstanceState != null && savedInstanceState.getSerializable(KEY_GAMEBOARD) != null) {
            continueGame((GameBoard) savedInstanceState.getSerializable(KEY_GAMEBOARD));
        }

        spacing = getResources().getDimension(R.dimen.spacing);

        messageView = (FontyTextView) v.findViewById(R.id.board_text);
        messageView.setText("");
        gridView = (GridView) v.findViewById(R.id.gridview);
        resetButton = v.findViewById(R.id.board_reset);
        playerOneView = (ImageView) v.findViewById(R.id.animation_image);
        playerTwoView = (ImageView) v.findViewById(R.id.animation_image2);

        resetButton.setVisibility(View.GONE);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetButton.setVisibility(View.GONE);
                resetBoard(getActivity(), mode);
                setupGridView(getActivity());
                startGame(getActivity(), mode);
                messageView.setText("");
            }
        });

        setupGridView(getActivity());
        return v;
    }

    public void startGame(Context context, Mode mode) {
        this.mode = mode;
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

        gridView.setNumColumns(gameBoard.getColNum());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

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
        step(position);
    }

    private void step(int position) {
        State currentState = gameBoard.getCurrentState();
        switch (currentState) {
            case PLACE_PIECE:
                gameBoard.setupPlayer(position);
                adapter.notifyDataSetChanged();
                displayMessage();
                break;
            case TURN_PLAYER:
                takeTurn(position);
                break;
            case GAME_OVER:
                handleGameOver();
                break;
            default: break;
        }
    }

    /** @return true if the turn was valid. */
    private void takeTurn(int position) {
        Triplet<Integer, Integer, Integer> t = gameBoard.takeTurn(position);

        if (t == null) {
            displayInvalidMessage();
        } else if (gameBoard.getCurrentState() == State.GAME_OVER) {
            handleGameOver();
            animateTurn(t);
        } else {
            displayMessage();
            animateTurn(t);
        }
    }

    /** Animates a player piece to its new grid tile. */
    private void animateTurn(Triplet<Integer, Integer, Integer> t) {
        final View tile;
        final View piece;

        if (t.getA() == 0) {
            piece = playerOneView;
            tile = adapter.getPlayerOneTile();
        } else {
            piece = playerTwoView;
            tile = adapter.getPlayerTwoTile();
        }

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
//                piece.bringToFront();
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
