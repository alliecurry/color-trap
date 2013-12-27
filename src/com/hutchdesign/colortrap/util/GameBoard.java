package com.hutchdesign.colortrap.util;

import android.content.Context;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class GameBoard {
    /** Default amount of tiles (including disabled). */
    private static final int DEFAULT_ROW_NUM = 6;
    private static final int DEFAULT_COL_NUM = 5;
    /** Tile positions disabled by default. */
    private static final int[] DEFAULT_DISABLED_TILES = {0, 2, 3, 4, 25};
    private List<List<Tile>> mTiles;
    private int rowNum;
    private int colNum;

    public GameBoard(Context c){
        setupTiles(c);
    }

    private void setupTiles(Context c){
        colNum = DEFAULT_COL_NUM;
        rowNum = DEFAULT_ROW_NUM;
        mTiles = createTiles(c, rowNum, colNum);
        assignColors(c, mTiles);
        assignDisabledTiles(mTiles);
    }

    private static List<List<Tile>> createTiles(Context c, int rowAmount, int colAmount) {
        List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        for (int i = 0; i < rowAmount; ++i) {
            tiles.add(i, new ArrayList<Tile>());
            for (int j = 0; j < colAmount; ++j)
            {
                tiles.get(i).add(j, new Tile(c));
            }
        }
        return tiles;
    }

    private void assignDisabledTiles(List<List<Tile>> tiles) {
        assignDisabledTiles(tiles, DEFAULT_DISABLED_TILES);
    }

    private void assignDisabledTiles(List<List<Tile>> tiles, int[] disabled) {
        for(int i=0; i<disabled.length; ++i) {
            tiles.get(disabled[i]/colNum).get(disabled[i] % colNum).disable();
        }
    }

    private void assignColors(Context c, List<List<Tile>> tiles) {
        int[] colors = getColors(c);
        Stack<Integer> colorsStack = new Stack<Integer>();

        // Convert colors array to a stack of colors equal to the amount of colors
        int i = 0;
        while(colorsStack.size() < colNum * rowNum) {
            colorsStack.add(colors[i]);
            i = (i == colors.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(colorsStack); // randomize colors
        for (i = 0; i < tiles.size(); i++){
            for (Tile t : tiles.get(i)) { // apply colors to tiles
            t.setColor(colorsStack.pop());
            }
        }
    }

    /** @return the default color palette in the form of resource ("R") values. */
    private static int[] getColors(Context c) {
        return c.getResources().getIntArray(R.array.grid_colors);
    }

    public int getColNum(){
        return colNum;
    }

    //Convert 2d List to single List for GridView display
    public Tile getGridPosition(int position){
        return mTiles.get(position/colNum)
        .get(position%colNum);
    }

    public int boardSize(){
        return colNum*rowNum;
    }
}