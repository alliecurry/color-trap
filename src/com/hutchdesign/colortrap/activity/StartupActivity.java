package com.hutchdesign.colortrap.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.model.Mode;
import com.hutchdesign.colortrap.util.FragmentUtility;

public class StartupActivity extends Activity {

    private GameFragment gameFragment;
    private boolean isGameStarted = false;

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

    // TODO send bundle with game state
    private void startGame() {
        findViewById(R.id.fragment).setVisibility(View.VISIBLE);
        FragmentUtility.replaceFragmentIgnoreStack(this, gameFragment, R.id.fragment);
        gameFragment.startGame(this, Mode.COMPUTER);
        isGameStarted = true;
    }

    private void endGame() {
        FragmentUtility.removeFragment(this, gameFragment);
        findViewById(R.id.fragment).setVisibility(View.GONE); // TODO Animate
        isGameStarted = false;
    }

    @Override
    public void onBackPressed() {
        if (isGameStarted) {
            endGame();
        } else {
            super.onBackPressed();
        }
    }
}
