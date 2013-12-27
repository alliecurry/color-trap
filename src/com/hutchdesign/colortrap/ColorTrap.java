package com.hutchdesign.colortrap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import com.hutchdesign.colortrap.util.GameBoard;

import java.util.List;

/**
 * Created by mike.hutcheson on 12/26/13.
 */
public class ColorTrap extends Activity {

    private Context mContext;

    private int[] mColors;
    GameBoard mTiles;

    public ColorTrap(){
    }

    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mContext = this;
        setContentView(R.layout.grid);
        mColors = mContext.getResources().getIntArray(R.array.grid_colors);
        mTiles = new GameBoard(mContext);
        setupGridView();
    }

    private void setupGridView(){
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setNumColumns(mTiles.getDefaultColNum());
        gridView.setAdapter(new GridAdapter(mContext, mTiles));
    }
}
