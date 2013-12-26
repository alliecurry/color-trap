package com.hutchdesign.colortrap;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

public class Tile extends View {

    private int mColor;
    private int mPosition;
    private boolean disabled;


    public Tile(Context c) {
        super(c);
        // TODO make sizing dynamic
        disabled = false;
        setLayoutParams(new AbsListView.LayoutParams(50, 50));
    }

    public void setColor(int color) {
        mColor = color;
        setBackgroundColor(color);
    }

    public void disable() {
        setBackgroundResource(android.R.color.transparent);
        setOnClickListener(null);
        setDisabled();

    }

    public boolean isDisabled() {
        return this.disabled;
    }

    private void setDisabled() {
        this.disabled = true;
    }
    // TODO implement me


}
