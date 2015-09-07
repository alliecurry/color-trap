package co.starsky.colortrap.model;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 544431964317821331L;

    /** A number corresponding to this tile's appearance.*/
    private int type;

    /** True when this tile is to be hidden. */
    private boolean disabled = false;

    public Tile(int type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void disable() {
        disabled = true;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

}
