package com.hutchdesign.colortrap.util;

import android.content.Context;
import com.hutchdesign.colortrap.R;

import java.util.Collections;
import java.util.Stack;

/**
 * @author alliecurry
 */
public final class TileUtility {

    private TileUtility() {
        throw new AssertionError();
    }

    /** Creates a randomized stack of the default color palette. */
    public static Stack<Integer> generateColors(Context c, int boardSize) {
        int[] colors = getColors(c);
        Stack<Integer> colorsStack = new Stack<Integer>();

        // Convert colors array to a stack of colors equal to the amount of colors
        int i = 0;
        while(colorsStack.size() < boardSize) {
            colorsStack.add(colors[i]);
            i = (i == colors.length - 1) ? 0 : i+1;
        }

        Collections.shuffle(colorsStack); // randomize colors
        return colorsStack;
    }

    /** @return the default color palette in the form of resource ("R") values. */
    private static int[] getColors(Context c) {
        return c.getResources().getIntArray(R.array.grid_colors2);
    }

}
