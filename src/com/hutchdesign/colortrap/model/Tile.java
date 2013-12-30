package com.hutchdesign.colortrap.model;

import com.hutchdesign.colortrap.R;

public class Tile {

    private int color;
    private boolean disabled = false;

    public Tile(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void disable() {
        color = R.color.board_background;
        disabled = true;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

}
