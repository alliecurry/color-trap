package com.hutchdesign.colortrap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

public class StartupActivity extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO show menu, not playing grid
        setContentView(R.layout.grid);

       GridView gridView = (GridView) findViewById(R.id.gridview);
       gridView.setNumColumns(5); // TODO move this to game setup function
       gridView.setAdapter(new GridAdapter(this));
    }
}
