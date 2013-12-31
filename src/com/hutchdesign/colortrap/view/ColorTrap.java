package com.hutchdesign.colortrap.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.GameBoard;
import com.hutchdesign.colortrap.model.State;


/**
 * Created by mike.hutcheson on 12/26/13.
 */
public class ColorTrap extends Activity implements AdapterView.OnItemClickListener {

    private Context context;
    GameBoard gameBoard;
    State state;
    GridView gridView;
    static final int PLAYER_ONE = 0;
    static final int PLAYER_TWO = 1;

    public ColorTrap(){
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        context = this;
        setContentView(R.layout.board);
        gameBoard = new GameBoard(context);
        setupGridView();
        state = State.PLACE_PIECE1;
    }

    private void setupGridView(){
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(gameBoard.getColNum());
        gridView.setAdapter(new GridAdapter(context, gameBoard));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Tile Clicked #" + position + "NEW STATE = " + state);
        step(position);
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }

    private void step(int position){
        switch(state){
            case PLACE_PIECE1:
                gameBoard.setupPlayer(PLAYER_ONE, position);

                state = State.PLACE_PIECE2;
                break;
            case PLACE_PIECE2:
                gameBoard.setupPlayer(PLAYER_TWO, position);
                state = State.TURN_PLAYER1;
                break;
            case TURN_PLAYER1:
                gameBoard.takeTurn(PLAYER_ONE, position);
                state = State.TURN_PLAYER2;
                break;
            case TURN_PLAYER2:
                gameBoard.takeTurn(PLAYER_TWO, position);
                state = State.TURN_PLAYER1;

                break;
            default: break;
        }

    }
}
