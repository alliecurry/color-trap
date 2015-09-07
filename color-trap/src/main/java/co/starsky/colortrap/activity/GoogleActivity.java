package co.starsky.colortrap.activity;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import co.starsky.colortrap.CTApplication;
import co.starsky.colortrap.model.GameOverData;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.service.GAService;
import co.starsky.colortrap.service.GameStatusReceiver;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

/**
 * @author alliecurry
 */
public class GoogleActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {
    private static final String TAG = GoogleActivity.class.getSimpleName();

    protected GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /* A flag indicating that a PendingIntent is in progress and prevents
         * us from starting further intents. */
    private boolean intentInProgress;

    /* Store the connection result from onConnectionFailed callbacks so that we can
     * resolve them when the user clicks sign-in. */
    private ConnectionResult connectionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerGameStatusReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "GameStatusReceiver UN-REGISTER");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(statusReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    /** A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInErrors() {
        if (connectionResult != null && connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                connectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent. Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                intentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "Google Plus Connection Failed");
        if (result != null && (result.getErrorCode() == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED
                || result.getErrorCode() == ConnectionResult.SERVICE_INVALID
                || result.getErrorCode() == ConnectionResult.SERVICE_DISABLED
                || result.getErrorCode() == ConnectionResult.SERVICE_MISSING)) {
            isDeviceSupported(this); // Check if device is supported (will show dialog if not)
        }
        if (!intentInProgress) {
            // Store the ConnectionResult so that we can use it later when the user clicks 'sign-in'.
            connectionResult = result;

//            if (signInClicked) {
                // The user has already clicked 'sign-in' so we attempt to resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInErrors();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean isDeviceSupported(Activity context) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode) && !context.isFinishing()) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    protected void unlockAchievement(final String id) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Achievements.unlock(mGoogleApiClient, id);
        }
    }

    protected void incrementAchievement(final String id, final int amount) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Games.Achievements.increment(mGoogleApiClient, id, amount);
        }
    }

    protected void incrementAchievement(final String id) {
        incrementAchievement(id, 1);
    }

    protected Tracker getTracker() {
        if (getApplication() instanceof CTApplication) {
            return ((CTApplication) getApplication()).getTracker();
        }
        return null;
    }

    private void registerGameStatusReceiver() {
        Log.d(TAG, "GameStatusReceiver GAME REGISTER");
        final IntentFilter intentFilter = new IntentFilter(GameStatusReceiver.ACTION_GAME_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(statusReceiver, intentFilter);
    }

    private GameStatusReceiver statusReceiver = new GameStatusReceiver() {
        private int lastGameId = -1;

        @Override
        public void onGameStart(Mode mode) {
            Log.d(TAG, "GameStatusReceiver GAME START");
            if (mode == Mode.COMPUTER) {
                GAService.trackGameStart(getTracker(), false);
            } else {
                GAService.trackGameStart(getTracker(), true);
            }
        }

        @Override
        public void onGameOver(Mode mode, GameOverData data) {
            Log.d(TAG, "GameStatusReceiver GAME OVER " + data.getGameId());
            if (data.getGameId() == lastGameId) {
                return; // We already tracked the Game Over for this session
            }
            lastGameId = data.getGameId();
            GAService.trackGameOver(getTracker(), mode != Mode.COMPUTER, data.getWinner().getName());
        }

        @Override
        public void onMoveInvalid(Mode mode, int currentTileIndex) {
            Log.d(TAG, "GameStatusReceiver MOVE INVALID");
        }
    };
}
