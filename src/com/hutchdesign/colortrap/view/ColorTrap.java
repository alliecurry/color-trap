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
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.model.State;


/**
 * Created by mike.hutcheson on 12/26/13.
 */
public class ColorTrap extends Activity implements AdapterView.OnItemClickListener {

    private Context context;
    GameBoard gameBoard;
    GridView gridView;
    Mode mode;
    public ColorTrap(){
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        context = this;
        mode = Mode.COMPUTER;
        setContentView(R.layout.board);
        gameBoard = new GameBoard(context, mode);
        setupGridView();
        //state = State.PLACE_PIECE1;
    }

    private void setupGridView(){
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(gameBoard.getColNum());
        gridView.setAdapter(new GridAdapter(context, gameBoard));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //System.out.println("Tile Clicked #" + position + "NEW STATE = " + state);
        step(position);
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();
    }

    private void step(int position){
        switch(gameBoard.getCurrentState()){
            case PLACE_PIECE:
                gameBoard.setupPlayer(position);
                break;
            case TURN_PLAYER:
                gameBoard.takeTurn(position);
                break;
            case GAME_OVER:
                recreate();
                break;

            default: break;
        }

    }
}
