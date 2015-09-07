package co.starsky.colortrap.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import co.starsky.colortrap.model.GameOverData;
import co.starsky.colortrap.model.Mode;

/**
 * @author alliecurry
 */
public abstract class GameStatusReceiver extends BroadcastReceiver {
    private static final String TAG = GameStatusReceiver.class.getSimpleName();
    public static final String ACTION_GAME_UPDATE = "co.starsky.colortrap.UPDATE";
    private static final String KEY_MESSAGE = "msg";
    private static final String KEY_MODE = "mde";
    private static final String KEY_VALUE = "vlu";

    private static Intent initIntent(final Mode mode) {
        final Intent intent = new Intent(ACTION_GAME_UPDATE);
        intent.putExtra(KEY_MODE, mode.ordinal());
        return intent;
    }

    public static Intent getGameStartIntent(final Mode mode) {
        final Intent i = initIntent(mode);
        i.putExtra(KEY_MESSAGE, Message.GAME_START.ordinal());
        return i;
    }

    public static Intent getGameOverIntent(final Mode mode, final GameOverData data) {
        final Intent i = initIntent(mode);
        i.putExtra(KEY_MESSAGE, Message.GAME_OVER.ordinal());
        i.putExtra(KEY_VALUE, data);
        return i;
    }

    public static Intent getInvalidMoveIntent(final Mode mode, final int currentTileIndex) {
        final Intent i = initIntent(mode);
        i.putExtra(KEY_MESSAGE, Message.MOVE_INVALID.ordinal());
        i.putExtra(KEY_VALUE, currentTileIndex);
        return i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || !intent.hasExtra(KEY_MESSAGE) || !intent.hasExtra(KEY_MODE)) {
            Log.w(TAG, "Invalid Bundle");
            return;
        }

        final Message message = Message.values()[intent.getIntExtra(KEY_MESSAGE, -1)];
        final Mode mode = Mode.values()[intent.getIntExtra(KEY_MODE, 0)];

        switch (message) {
            case GAME_START:
                onGameStart(mode);
                break;
            case GAME_OVER:
                onGameOver(mode, (GameOverData) intent.getSerializableExtra(KEY_VALUE));
                break;
            case MOVE_INVALID:
                onMoveInvalid(mode, intent.getIntExtra(KEY_VALUE, -1));
                break;
        }
    }

    public abstract void onGameStart(final Mode mode);
    public abstract void onGameOver(final Mode mode, final GameOverData data);
    public abstract void onMoveInvalid(final Mode mode, final int currentTileIndex);

    private enum Message {
        GAME_START, GAME_OVER, MOVE_INVALID
    }
}
