package com.hutchdesign.colortrap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.util.FragmentUtility;

public class StartupActivity extends Activity implements View.OnClickListener {

    private GameFragment gameFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        findViewById(R.id.button_new_computer).setOnClickListener(this);
        findViewById(R.id.button_new_hotseat).setOnClickListener(this);
        gameFragment = new GameFragment();
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
