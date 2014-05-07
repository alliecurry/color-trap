package co.starsky.colortrap.util;

import android.content.Context;
import android.content.res.Resources;
import co.starsky.colortrap.R;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.model.State;

import java.util.LinkedList;

/**
 * Determines what message to display to the user.
 * @author alliecurry
 */
public class MessageHelper {
    private Mode mode;
    private LinkedList<String> placePieceStack;
    private LinkedList<String> turnStack;
    private LinkedList<String> invalidStack;
    private LinkedList<String> gameOverStack;

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

        invalidStack = Shuffle.shuffleString(res.getStringArray(R.array.invalid_list));
        gameOverStack = Shuffle.shuffleString(res.getStringArray(R.array.game_over_list));
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

    public String getInvalidMessage() {
        return getNextMessage(invalidStack);
    }

    public String getGameOverMessage(String playerName) {
        return String.format(getNextMessage(gameOverStack), playerName);
    }

    public String getNextMessage(LinkedList<String> list) {
        String popped = list.pop();
        list.add(popped);
        return popped;
    }

}
