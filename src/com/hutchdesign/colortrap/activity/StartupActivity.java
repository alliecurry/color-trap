package com.hutchdesign.colortrap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.util.FragmentUtility;

public class StartupActivity extends Activity {

    private GameFragment gameFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        View newGame = findViewById(R.id.menu_text_new);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        gameFragment = new GameFragment();
    }

    private void startGame() {
        FragmentUtility.replaceFragment(this, gameFragment, R.id.fragment, null);
        gameFragment.startGame(this, Mode.COMPUTER); // TODO have mode chosen from menu
    }

    @Override
    public void onBackPressed() {
        if (FragmentUtility.getBackStackCount(this) > 0) {
            FragmentUtility.removeFragment(this, gameFragment);
        } else {
            super.onBackPressed();
        }
    }
}
