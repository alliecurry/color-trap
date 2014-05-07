package co.starsky.colortrap.model;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 544431964317821331L;

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
        disabled = true;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

}
