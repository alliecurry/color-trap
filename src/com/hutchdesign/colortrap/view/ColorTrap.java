package com.hutchdesign.colortrap.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.GameBoard;


/**
 * Created by mike.hutcheson on 12/26/13.
 */
public class ColorTrap extends Activity {

    private Context mContext;

    //private int[] mColors;
    GameBoard mTiles;

    public ColorTrap(){
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mContext = this;
        setContentView(R.layout.board);
        mTiles = new GameBoard(mContext);
        setupGridView();
    }

    private void setupGridView(){
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(mTiles.getColNum());
        gridView.setAdapter(new GridAdapter(mContext, mTiles));
    }
}
