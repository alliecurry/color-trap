package com.hutchdesign.colortrap.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.hutchdesign.colortrap.R;

public class StartupActivity extends Activity {

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

    }

    // TODO send bundle with game state
    private void startGame() {
        Intent i = new Intent(this, ColorTrap.class);
        startActivity(i);
    }
}
