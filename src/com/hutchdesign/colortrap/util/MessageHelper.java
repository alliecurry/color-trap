package com.hutchdesign.colortrap.util;

import android.content.Context;
import android.content.res.Resources;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.model.State;

import java.util.LinkedList;

/**
 * Determines what message to display to the user.
 * @author alliecurry
 */
public class MessageHelper {
    private Mode mode;
    private LinkedList<String> placePieceStack;
    private LinkedList<String> turnStack;

    String player1 = "Player 1";
    String player2 = "Player 2";


    public void setMode(Context c, Mode mode) {
        this.mode = mode;
        Resources res = c.getResources();
        switch (mode) {
            case HOTSEAT:
                placePieceStack = Shuffle.shuffleString(res.getStringArray(R.array.place_piece_hotseat_list));
                turnStack = Shuffle.shuffleString(res.getStringArray(R.array.turn_hotseat_list));
                break;
            case COMPUTER:
                placePieceStack = Shuffle.shuffleString(res.getStringArray(R.array.place_piece_list));
                turnStack = Shuffle.shuffleString(res.getStringArray(R.array.turn_list));
                break;
        }
    }

    public void setPlayerNames(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public String getMessage(State state, String playerName) {
        return String.format(getMessage(state), playerName);
    }

    public String getMessage(State state) {
        switch (state) {
            case PLACE_PIECE:
                return getNextMessage(placePieceStack);
            case TURN_PLAYER:
                return getNextMessage(turnStack);
        }

        return "";
    }

    public String getNextMessage(LinkedList<String> list) {
        String popped = list.pop();
        list.add(popped);
        return popped;
    }


}
