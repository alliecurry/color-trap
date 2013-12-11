package com.hutchdesign.colortrap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {
    private static final int TOTAL_TILES = 36;

    /** Indices corresponding to blank tiles. */
    private static final int[] DISABLED_TILES = {0, 2, 3, 4, 31};

    private Context mContext;
    private int[] mColors;

    public GridAdapter(Context c) {
        mContext = c;
        mColors = c.getResources().getIntArray(R.array.grid_colors);
    }

    public int getCount() {
        return TOTAL_TILES;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Tile coloredTile;

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            coloredTile = new Tile(mContext);
            coloredTile.setBackgroundResource(getColors()[position]);
            // TODO set colors
        } else {
            coloredTile = (Tile) convertView;
        }


        return coloredTile;
    }

}