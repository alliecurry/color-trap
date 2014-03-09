package com.hutchdesign.colortrap.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.hutchdesign.colortrap.view.FontyTextView;
import com.hutchdesign.colortrap.view.GridAdapter;

/**
 * Activity which starts and manages a new game.
 */
public class GameFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GameBoard gameBoard;
    private GridView gridView;
    private FontyTextView messageView;
    private Mode mode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.board, container, false);
        messageView = (FontyTextView) v.findViewById(R.id.board_text);
        gridView = (GridView) v.findViewById(R.id.gridview);
        setupGridView(getActivity());
        return v;
    }

    public void startGame(Context context, Mode mode) {
        this.mode = mode;
        gameBoard = new GameBoard(context, mode);
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
                break;
            case TURN_PLAYER:
                gameBoard.takeTurn(position);
                if (currentState == State.GAME_OVER) {
                    handleGameOver();
                }
                break;
            case GAME_OVER:
                handleGameOver();
                break;
            default: break;
        }
        displayMessage(currentState);
    }

    /** Display some message to the user based on the given State. */
    private void displayMessage(State state) {
        messageView.setText("");

        // TODO cross-fade messages
    }

    private void handleGameOver() {
        // TODO
        messageView.setText("Game Over");
    }


}
