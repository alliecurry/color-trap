package com.hutchdesign.colortrap.activity;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.util.FragmentUtility;
import com.hutchdesign.colortrap.view.RotateTouchListener;

public class StartupActivity extends Activity implements View.OnClickListener {

    private GameFragment gameFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        RotateTouchListener touchListener = getTouchAnimationListener();

        final View buttonHotseat = findViewById(R.id.button_new_hotseat);
        final View buttonComputer = findViewById(R.id.button_new_computer);
        final View buttonHelp = findViewById(R.id.button_help);

        buttonHotseat.setOnClickListener(this);
        buttonComputer.setOnClickListener(this);

        buttonHotseat.setOnTouchListener(touchListener);
        buttonComputer.setOnTouchListener(touchListener);
        buttonHelp.setOnTouchListener(touchListener);

        gameFragment = new GameFragment();
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

    private void startGame(Mode mode) {
        FragmentUtility.replaceFragment(this, gameFragment, R.id.fragment, null);
        gameFragment.startGame(this, mode);
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
