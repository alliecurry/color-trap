package com.hutchdesign.colortrap.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.controller.GridClickListener;
import com.hutchdesign.colortrap.model.GameBoard;


/**
 * Created by mike.hutcheson on 12/26/13.
 */
public class ColorTrap extends Activity {

    private Context context;
    GameBoard gameBoard;

    public ColorTrap(){
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        context = this;
        setContentView(R.layout.board);
        gameBoard = new GameBoard(context);
        setupGridView();
    }

    private void setupGridView(){
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(gameBoard.getColNum());
        gridView.setAdapter(new GridAdapter(context, gameBoard));
        gridView.setOnItemClickListener(new GridClickListener());
    }
}
