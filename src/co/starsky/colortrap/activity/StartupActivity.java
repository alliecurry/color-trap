package co.starsky.colortrap.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import co.starsky.colortrap.R;
import co.starsky.colortrap.dialog.HelpDialog;
import co.starsky.colortrap.game.GameFragment;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.util.FragmentUtility;
import co.starsky.colortrap.view.RotateTouchListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import co.starsky.colortrap.util.Prefs;


public class StartupActivity extends Activity implements View.OnClickListener {

    private GameFragment gameFragment;
    private InterstitialAd interstitial;
    private static final String NEW_GAME_AD_ID = "ca-app-pub-4074371291605833/9344298102";
    private static String TAG = StartupActivity.class.getSimpleName();

    private View buttonHotseat;
    private View buttonComputer;
    private View buttonHelp;

    private boolean isFirstLaunch = false;
    private boolean didShowHelp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        setupAd();

        buttonHotseat = findViewById(R.id.button_new_hotseat);
        buttonComputer = findViewById(R.id.button_new_computer);
        buttonHelp = findViewById(R.id.button_help);

        buttonHotseat.setOnClickListener(this);
        buttonComputer.setOnClickListener(this);
        buttonHelp.setOnClickListener(this);

        RotateTouchListener touchListener = getTouchAnimationListener();
        buttonHotseat.setOnTouchListener(touchListener);
        buttonComputer.setOnTouchListener(touchListener);
        buttonHelp.setOnTouchListener(touchListener);

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment);
        if (currentFragment instanceof GameFragment) {
            gameFragment = (GameFragment) currentFragment;
        } else {
            gameFragment = new GameFragment();
        }

        isFirstLaunch = Prefs.isFirstPlay(this);
    }

    private RotateTouchListener getTouchAnimationListener() {
        final ObjectAnimator animatorLeft = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_left);
        final ObjectAnimator animatorRight = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_right);
        final ObjectAnimator animatorLeftReset = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_left_reset);
        final ObjectAnimator animatorRightReset = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_right_reset);
        final RotateTouchListener touchListener = new RotateTouchListener(animatorLeft, animatorRight, getResources().getInteger(R.integer.rotation_menu_duration));
        touchListener.setResetAnimators(animatorLeftReset, animatorRightReset);
        return touchListener;
    }

    /** Remove the Y rotation from the menu buttons. */
    private void resetButtonAnimation() {
        // Only need to do this for older APIs...
        int api = Build.VERSION.SDK_INT;
        if (api >= 18){ //Build.VERSION_CODES.KITKAT doesnt exist in minSDK 15
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonHotseat.setRotationY(0f);
                buttonComputer.setRotationY(0f);
                buttonHelp.setRotationY(0f);
            }
        }, 500);
    }

    private void setupAd() {
        // Create the interstitial.
        try{
            interstitial = new InterstitialAd(this);
            interstitial.setAdUnitId(NEW_GAME_AD_ID);

            // Create ad request.
            AdRequest adRequest = new AdRequest.Builder().build();

            // Begin loading your interstitial.
            interstitial.loadAd(adRequest);
        }
        catch(Exception adErr){ Log.e(TAG, adErr.toString()); }
    }

    public void displayAd() {
        if (isFirstLaunch) {
            return;
        }

        try {
            displayInterstitial();
        } catch (Exception adErr) {
            Log.e(TAG, adErr.toString());
        }
    }

    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void startGame(final Mode mode) {
        displayAd();
        gameFragment.resetBoard(this, mode);
        gameFragment.startGame(this, mode);
        showGameFragment();
        // Show Help dialog if this is the first play.
        if (isFirstLaunch && !didShowHelp) {
            showHelp();
            didShowHelp = true;
        }
    }

    private void showGameFragment() {
        FragmentUtility.replaceFragment(this, gameFragment, R.id.fragment, null);
    }

    private void showHelp() {
        new HelpDialog().show(getFragmentManager(), HelpDialog.class.getSimpleName());
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Starting analytics");
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        if (isFirstLaunch) {
            Prefs.setFirstPlay(this);
        }
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void onClick(View view) {
        EasyTracker easyTracker = EasyTracker.getInstance(this);
        switch (view.getId()) {
            case R.id.button_new_computer:
                easyTracker.send(MapBuilder.createEvent("Game_Info","Mode_Select",
                        "Single_Player", null).build());
                startGame(Mode.COMPUTER);
                break;
            case R.id.button_new_hotseat:
                easyTracker.send(MapBuilder.createEvent("Game_Info","Mode_Select",
                        "Multi_Player", null).build());
                startGame(Mode.HOTSEAT);
                break;
            case R.id.button_help:
                showHelp();
                break;
            default: break;
        }

        resetButtonAnimation();
    }
}
