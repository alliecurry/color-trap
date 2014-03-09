package com.hutchdesign.colortrap.activity;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.GameBoard;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.model.State;
import com.hutchdesign.colortrap.util.MessageHelper;
import com.hutchdesign.colortrap.view.FontyTextView;
import com.hutchdesign.colortrap.view.GridAdapter;

/**
 * Activity which starts and manages a new game.
 */
public class GameFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final int FADE_DURATION = 300;

    private GameBoard gameBoard;
    private GridView gridView;
    private FontyTextView messageView;
    private MessageHelper msgHelper;
    private Mode mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.board, container, false);
        messageView = (FontyTextView) v.findViewById(R.id.board_text);
        gridView = (GridView) v.findViewById(R.id.gridview);
        setupGridView(getActivity());
        displayMessage(State.PLACE_PIECE);
        return v;
    }

    public void startGame(Context context, Mode mode) {
        this.mode = mode;
        gameBoard = new GameBoard(context, mode);

        if (msgHelper == null) {
            msgHelper = new MessageHelper();
        }

        msgHelper.setMode(context, mode);
    }

    private void setupGridView(Context context) {
        final GridAdapter adapter = new GridAdapter(context, gameBoard);
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
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }

    private void step(int position) {
        State currentState = gameBoard.getCurrentState();
        switch (currentState) {
            case PLACE_PIECE:
                gameBoard.setupPlayer(position);
                displayMessage(State.TURN_PLAYER);
                break;
            case TURN_PLAYER:
                State newState = gameBoard.takeTurn(position);
                if (newState == State.GAME_OVER) {
                    handleGameOver();
                } else {
                    displayMessage(newState);
                }
                break;
            case GAME_OVER:
                handleGameOver();
                break;
            default: break;
        }
    }

    private void fadeOutView(View v) {
        ObjectAnimator animationFadeOut = ObjectAnimator.ofFloat(v, "alpha", 1f, 0f);
        animationFadeOut.setDuration(FADE_DURATION);
        animationFadeOut.start();
    }

    private void fadeInView(View v) {
        ObjectAnimator animationFadeIn = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        animationFadeIn.setDuration(FADE_DURATION);
        animationFadeIn.start();
    }

    /** Display some message to the user based on the given State. */
    private void displayMessage(State state) {
        final String message = msgHelper.getMessage(state);
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
        // TODO
        Log.d("allie", "game over");
        messageView.setText("Game Over");
    }


}
