package com.hutchdesign.colortrap.model;

/**
 * Created by mike.hutcheson on 1/5/14.
 */
public class ComputerPlayer extends Player {
    private static final long serialVersionUID = -4804780766424367597L;

    public ComputerPlayer(int position, boolean isFirstPlayer) {
        super(position, isFirstPlayer);
        setName("Computer");
    }
}
