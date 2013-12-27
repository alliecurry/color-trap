package com.hutchdesign.colortrap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartupActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO show menu, start button creates new grid

        Intent i = new Intent(this, ColorTrap.class);
        startActivity(i);
//       GridView gridView = (GridView) findViewById(R.id.gridview);
//       gridView.setNumColumns(5);
//       gridView.setAdapter(new GridAdapter(this));
    }
}
