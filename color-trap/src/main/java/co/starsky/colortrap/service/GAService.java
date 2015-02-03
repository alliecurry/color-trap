package co.starsky.colortrap.service;

import android.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Class for making API calls to Google Analytics.
 * @author alliecurry
 */
public final class GAService {

    private static final String CATEGORY_GAME = "Game";
    private static final String ACTION_START = "Start";
    private static final String ACTION_WIN = "Single Player Win";
    private static final String ACTION_MULTI_WIN = "Multiplayer Win";
    private static final String LABEL_SINGLE_PLAYER = "Single Player";
    private static final String LABEL_MULTIPLAYER = "Multiplayer";

    /** Sends a screen tracking view to Google Analytics. */
    public static void sendScreenView(final Tracker t, final String screen) {
        t.setScreenName(screen);
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    /** Sends a Google Analytics Event. Label is optional. */
    public static void sendEvent(final Tracker t, final String category, final String action,
                                 final String label) {
        sendEvent(t, category, action, label, null);
    }

    /** Sends a Google Analytics Event. Label and Value are optional. */
    public static void sendEvent(final Tracker t, final String category, final String action,
                                 final String label, final Integer value) {
        if (t == null) {
            Log.e("GAService", "Cannot Send GA Event: Tracker is null");
        }

        HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action);
        if (label != null && !label.isEmpty()) {
            builder = builder.setLabel(label);
        }
        if (value != null) {
            builder = builder.setValue(value);
        }
        t.send(builder.build());
    }

    public static void trackGameStart(final Tracker t, final boolean isMultiplayer) {
        sendEvent(t, CATEGORY_GAME, ACTION_START, isMultiplayer ? LABEL_MULTIPLAYER : LABEL_SINGLE_PLAYER);
    }

    public static void trackGameOver(final Tracker t, final boolean isMultiplayer,
                                     final String winner) {
        sendEvent(t, CATEGORY_GAME, isMultiplayer ? ACTION_MULTI_WIN : ACTION_WIN, winner);
    }
}
