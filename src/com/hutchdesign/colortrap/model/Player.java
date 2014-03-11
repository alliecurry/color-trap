package com.hutchdesign.colortrap.model;

public class Player {

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

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }
}
