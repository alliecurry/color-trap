package com.hutchdesign.colortrap;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

public class Tile extends View {

    private int mColor;
    private int mPosition;


    public Tile(Context c) {
        super(c);
        // TODO make sizing dynamic
        setLayoutParams(new AbsListView.LayoutParams(50, 50));
    }

    public void setColor(int color) {
        mColor = color;
        setBackgroundColor(color);
    }

    public void disable() {
        setBackgroundResource(android.R.color.transparent);
        setOnClickListener(null);
    }

    // TODO implement me


}
