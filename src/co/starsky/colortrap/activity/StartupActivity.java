package co.starsky.colortrap.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import co.starsky.colortrap.R;
import co.starsky.colortrap.model.Mode;
import co.starsky.colortrap.util.FragmentUtility;
import co.starsky.colortrap.view.RotateTouchListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import co.starsky.colortrap.util.Prefs;


public class StartupActivity extends Activity implements View.OnClickListener {

    private GameFragment gameFragment;
    private InterstitialAd interstitial;
    private static final String NEW_GAME_AD_ID = "ca-app-pub-4074371291605833/9344298102";
    private static String TAG = StartupActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

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

        RotateTouchListener touchListener = getTouchAnimationListener();

        final View buttonHotseat = findViewById(R.id.button_new_hotseat);
        final View buttonComputer = findViewById(R.id.button_new_computer);
        final View buttonHelp = findViewById(R.id.button_help);

        buttonHotseat.setOnClickListener(this);
        buttonComputer.setOnClickListener(this);

        buttonHotseat.setOnTouchListener(touchListener);
        buttonComputer.setOnTouchListener(touchListener);
        buttonHelp.setOnTouchListener(touchListener);

        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.fragment);
        if (currentFragment instanceof GameFragment) {
            gameFragment = (GameFragment) currentFragment;
        } else {
            gameFragment = new GameFragment();
        }

    }

    private RotateTouchListener getTouchAnimationListener() {
        final ObjectAnimator animatorLeft = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_left);
        final ObjectAnimator animatorRight = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_right);
        final ObjectAnimator animatorLeftReset = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_left_reset);
        final ObjectAnimator animatorRightReset = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.rotate_right_reset);
        RotateTouchListener touchListener = new RotateTouchListener(animatorLeft, animatorRight, getResources().getInteger(R.integer.rotation_menu_duration));
        touchListener.setResetAnimators(animatorLeftReset, animatorRightReset);
        return touchListener;
    }

    public void displayAd() {
        if (Prefs.isFirstPlay(this)) {
            Prefs.setFirstPlay(this);
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

    private void startGame(Mode mode) {
//        displayAd();
        FragmentUtility.replaceFragment(this, gameFragment, R.id.fragment, null);
        gameFragment.resetBoard(this, mode);
        gameFragment.startGame(this, mode);

    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Starting analytics");
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
    @Override
    public void onBackPressed() {
        if (FragmentUtility.getBackStackCount(this) > 0) {
            FragmentUtility.removeFragment(this, gameFragment);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_computer:
                startGame(Mode.COMPUTER);
                break;
            case R.id.button_new_hotseat:
                startGame(Mode.HOTSEAT);
                break;
            default: break;
        }
    }
}
