package com.hutchdesign.colortrap.util;

import android.content.Context;
import android.content.res.Resources;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.model.State;

/**
 * Determines what message to display to the user.
 * @author alliecurry
 */
public class MessageHelper {
    private Mode mode;

    public MessageHelper(Context c) {
        Resources res = c.getResources();

    }

    public void setMode(Context c, Mode mode) {

    }

    public static String getMessage(Mode mode, State state) {
        switch (mode) {
            case HOTSEAT:
                return getMessageForHotseat(state);
            case COMPUTER:
                return getMessageForBot(state);
        }
        return "";
    }

    public static String getMessageForHotseat(State state) {



        return "";
    }

    public static String getMessageForBot(State state) {

        return "";
    }


}
