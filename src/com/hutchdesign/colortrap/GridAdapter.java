package com.hutchdesign.colortrap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.hutchdesign.colortrap.util.GridUtility;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    List<Tile> mTiles;

    private Context mContext;
    private int[] mColors;

    public GridAdapter(Context c) {
        mContext = c;
        mColors = c.getResources().getIntArray(R.array.grid_colors);
        setupTiles();
    }

    private void setupTiles() {
        mTiles = GridUtility.createTiles(mContext);
        GridUtility.assignColors(mContext, mTiles);
        GridUtility.assignDisabledTiles(mTiles);
    }

    public int getCount() {
        return mTiles.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView == null ?  mTiles.get(position) : (Tile) convertView;
    }

}