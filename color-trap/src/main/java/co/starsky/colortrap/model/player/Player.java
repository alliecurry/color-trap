package co.starsky.colortrap.model.player;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = -9196884400202320545L;

    /** Players current grid position or -1 if not placed. */
    private int position;
    private boolean isFirstPlayer;
    private String name;

    public Player(int position, boolean isFirstPlayer) {
        this.position = position;
        this.isFirstPlayer = isFirstPlayer;
        this.name = "Player " + (isFirstPlayer ? "1" : "2");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public PlayerType getType() {
        return PlayerType.HUMAN;
    }
}
