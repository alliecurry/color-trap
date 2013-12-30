package com.hutchdesign.colortrap.controller;

import android.view.View;
import android.widget.AdapterView;

public class GridClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Tile Clicked #" + position);
    }
}
