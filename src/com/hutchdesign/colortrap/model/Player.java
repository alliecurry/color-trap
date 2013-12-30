package com.hutchdesign.colortrap.model;

public class Player {

    /** Players current grid position or -1 if not placed. */
    private int position;

    private boolean isFirstPlayer;

    public Player(int position, boolean isFirstPlayer) {
        this.position = position;
        this.isFirstPlayer = isFirstPlayer;
    }

    public Player(boolean isFirstPlayer) {
        this.position = -1;
        this.isFirstPlayer = isFirstPlayer;
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

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }
}
