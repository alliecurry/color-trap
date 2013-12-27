package com.hutchdesign.colortrap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.hutchdesign.colortrap.util.GameBoard;

public class GridAdapter extends BaseAdapter {
    GameBoard mTiles;
    private Context mContext;

    public GridAdapter(Context c, GameBoard tiles) {
        mContext = c;
        mTiles = tiles;
//        mColors = c.getResources().getIntArray(R.array.grid_colors);
//        setupTiles();
    }

//    private void setupTiles() {
//        mTiles = GridUtility.createTiles(mContext);
//        GridUtility.assignColors(mContext, mTiles);
//        GridUtility.assignDisabledTiles(mTiles);
//    }

    public int getCount() {
        return mTiles.boardSize();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView == null ?  mTiles.getGridPosition(position) : (Tile) convertView;
    }

}