package com.hutchdesign.colortrap;

import android.app.Activity;
import android.os.Bundle;

public class StartupActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO show menu, not playing grid
        setContentView(R.layout.grid);
    }
}
