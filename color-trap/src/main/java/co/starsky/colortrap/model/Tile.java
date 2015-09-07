package co.starsky.colortrap.model;

import java.io.Serializable;

public class Tile implements Serializable {
    private static final long serialVersionUID = 544431964317821331L;
    private static final int DISABLED = -1;

    /** A number corresponding to this tile's appearance. */
    private int type;

    public Tile(final int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void disable() {
        type = DISABLED;
    }

    public boolean isDisabled() {
        return type == DISABLED;
    }

}
