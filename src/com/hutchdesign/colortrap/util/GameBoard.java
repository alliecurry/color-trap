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
    private Tile[][] mTiles;
    private int rowNum;
    private int colNum;

    public GameBoard(Context c){
        setupTiles(c);
    }



    /*Initial Board setup Methods

    Creates board of row x col size Tiles, applies initial randomized colors and disabled tiles
    Board is a 2d List of Tiles (List of ArrayList of Tiles). Accessed similar to coordinates starting at 0,0
    in top left corner
    */
    private void setupTiles(Context c){
        colNum = DEFAULT_COL_NUM;
        rowNum = DEFAULT_ROW_NUM;
        createTiles(c, rowNum, colNum);
        assignColors(c);
        assignDisabledTiles();
    }

    private void createTiles(Context c, int rowAmount, int colAmount) {
        mTiles = new Tile[rowAmount][colAmount];
        for (int i = 0; i < rowAmount; ++i) {
            for (int j = 0; j < colAmount; ++j)
            {
                mTiles[i][j] = new Tile(c);
            }
        }
    }

    private void assignDisabledTiles() {
        assignDisabledTiles(DEFAULT_DISABLED_TILES);
    }

    private void assignDisabledTiles(int[] disabled) {
        for(int i=0; i<disabled.length; ++i) {
            getGridPosition(disabled[i]).disable();
        }
    }

    private void assignColors(Context c) {
        int[] colors = getColors(c);
        Stack<Integer> colorsStack = new Stack<Integer>();

        // Convert colors array to a stack of colors equal to the amount of colors
        int i = 0;
        while(colorsStack.size() < boardSize()) {
            colorsStack.add(colors[i]);
            i = (i == colors.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(colorsStack); // randomize colors
        for (Tile[] tArray : mTiles){
            for (Tile t : tArray) { // apply colors to tiles
            t.setColor(colorsStack.pop());
            }
        }
    }

    /** @return the default color palette in the form of resource ("R") values. */
    private static int[] getColors(Context c) {
        return c.getResources().getIntArray(R.array.grid_colors);
    }

    /** Utility methods and getter/setters */

    public int getColNum(){
        return colNum;
    }

    public int getRowNum(){
        return rowNum;
    }

    //Gets tile position based on 1d position for utility purposes
    public Tile getGridPosition(int position){
        return mTiles[(position/colNum)]
        [(position%colNum)];
    }

    public int boardSize(){
        return colNum*rowNum;
    }

    public Tile getTile(int row, int col){
        return mTiles[(row)][(col)];
    }
}