package co.starsky.colortrap.activity;

import android.animation.ObjectAnimator;
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
import co.starsky.colortrap.animator.PathPoint;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.model.State;
import co.starsky.colortrap.view.FontyTextView;
import co.starsky.colortrap.animator.AnimatorProxy;
import co.starsky.colortrap.animator.PathEvaluator;
import co.starsky.colortrap.model.GameBoard;
import co.starsky.colortrap.model.Triplet;
import co.starsky.colortrap.util.MessageHelper;
import co.starsky.colortrap.view.AnimatedAdapter;
import co.starsky.colortrap.view.GridAdapter;


/**
 * Activity which starts and manages a new game.
 */
public class GameFragment extends Fragment implements AdapterView.OnItemClickListener, AnimatedAdapter.AnimationListener {
    protected static final String KEY_GAMEBOARD = "gameb";
    private static final int FADE_DURATION = 300;
    private float spacing;

    private GameBoard gameBoard;
    private GridView gridView;
    private GridAdapter adapter;
    private FontyTextView messageView;
    private MessageHelper msgHelper;
    private View resetButton;
    private Mode mode;
    private ImageView animateView;

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
        animateView = (ImageView) v.findViewById(R.id.animation_image);

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
            animate(t);
        } else {
            displayMessage();
            animate(t);
        }
    }

    private void animate(Triplet<Integer, Integer, Integer> t) {
        int player = t.getA();
        View tile;

        if (player == 0) {
            animateView.setImageResource(R.drawable.player);
            tile = adapter.getPlayerOneTile();
        } else {
            animateView.setImageResource(R.drawable.player2);
            tile = adapter.getPlayerTwoTile();
        }

        // Grid View indices
        final float oldPos = (float) t.getB();
        final float newPos = (float) t.getC();

        // Screen start location
        final float x = tile.getX() + ((tile.getWidth() / 2) - (animateView.getWidth() / 2));
        final float y = tile.getY() + ((tile.getHeight() / 2) - (animateView.getHeight() / 2));

        // Tile size including grid padding
        final float oneTileX = tile.getWidth() + spacing;
        final float oneTileY = tile.getHeight() + spacing;

        final float cols = GameBoard.COL_NUM;
        final float n = (newPos - oldPos) / cols;
        final float vertical = (int) n; // # of columns move difference

        final float xOffset = oneTileX * ((n - vertical) * cols);
        final float yOffset = oneTileY * vertical;

        float xCurve = getCurve(oneTileX, xOffset);
        float yCurve = getCurve(oneTileY, yOffset);

        if (n < 0) {
            xCurve = -xCurve;
            yCurve = -yCurve;
        }

        adapter.hidePlayer(player);
        jump(x, y, xOffset, yOffset, xCurve, yCurve);
    }

    private void jump(final float x, final float y,
                      final float xOffset, final float yOffset,
                      final float xCurve, final float yCurve) {

        AnimatorPath path = new AnimatorPath();
        path.moveTo(x, y);
        path.curveTo(
                x + xCurve, y - yCurve,
                x + xOffset + xCurve, y + yOffset - yCurve,
                x + xOffset, y + yOffset);

        AnimatorProxy.wrap(animateView);

        // Set up the animation
        final ObjectAnimator anim = ObjectAnimator.ofObject(this, "buttonLocation",
                new PathEvaluator(), path.getPoints().toArray());

        final int duration = (int) Math.abs((xOffset + yOffset) / 3) + 250;
        anim.setDuration(duration);
        animateView.setAlpha(1f);
        anim.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { // Animation on finish
                animateView.setAlpha(0f);
                adapter.notifyDataSetChanged();
            }
        }, duration);
    }

    private static float getCurve(float tileSize, float offset) {
        return offset > -1f && offset < 1f ? (tileSize / 2) + offset : 0;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setButtonLocation(PathPoint newLoc) {
        animateView.setTranslationX(newLoc.mX);
        animateView.setTranslationY(newLoc.mY);
    }

    private static void fadeOutView(View v) {
        ObjectAnimator animationFadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        animationFadeOut.setDuration(FADE_DURATION);
        animationFadeOut.start();
    }

    private static void fadeInView(View v) {
        ObjectAnimator animationFadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        animationFadeIn.setDuration(FADE_DURATION);
        animationFadeIn.start();
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
        fadeOutView(messageView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                messageView.setText(message);
                fadeInView(messageView);
            }
        }, FADE_DURATION);
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
