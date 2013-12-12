package com.hutchdesign.colortrap.util;

import android.content.Context;
import com.hutchdesign.colortrap.R;
import com.hutchdesign.colortrap.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public final class GridUtility {
    /** Default amount of tiles (including disabled). */
    private static final int DEFAULT_TILE_AMOUNT = 30;

    /** Tile positions disabled by default. */
    private static final int[] DEFAULT_DISABLED_TILES = {0, 2, 3, 4, 25};

    public static List<Tile> createTiles(Context c) {
        return createTiles(c, DEFAULT_TILE_AMOUNT);
    }

    public static List<Tile> createTiles(Context c, int amount) {
        List<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < amount; ++i) {
            tiles.add(new Tile(c));
        }
        return tiles;
    }

    public static void assignDisabledTiles(List<Tile> tiles) {
        assignDisabledTiles(tiles, DEFAULT_DISABLED_TILES);
    }

    public static void assignDisabledTiles(List<Tile> tiles, int[] disabled) {
        for(int i=0; i<disabled.length; ++i) {
            tiles.get(disabled[i]).disable();
        }
    }

    public static void assignColors(Context c, List<Tile> tiles) {
        int[] colors = getColors(c);
        Stack<Integer> colorsStack = new Stack<Integer>();

        // Convert colors array to a stack of colors equal to the amount of colors
        int i = 0;
        while(colorsStack.size() < tiles.size()) {
            colorsStack.add(colors[i]);
            i = (i == colors.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(colorsStack); // randomize colors

        for (Tile t : tiles) { // apply colors to tiles
            t.setColor(colorsStack.pop());
        }
    }

    /** @return the default color palette in the form of resource ("R") values. */
    public static int[] getColors(Context c) {
        return c.getResources().getIntArray(R.array.grid_colors);
    }


}
