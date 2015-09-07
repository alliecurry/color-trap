package co.starsky.colortrap.view;

import android.content.Context;
import android.util.TypedValue;
import co.starsky.colortrap.R;
import co.starsky.colortrap.model.Tile;

/**
 * @author alliecurry
 */
public final class ColorHelper {
    private static int[] THEME;

    private ColorHelper() {
        throw new AssertionError();
    }

    public static int getColorForTile(final Context c, final Tile tile) {
        if (THEME == null) {
            final TypedValue tv = new TypedValue();
            c.getTheme().resolveAttribute(R.attr.colorTheme, tv, true);
            final int arrayId = tv.resourceId;
            THEME = c.getResources().getIntArray(arrayId);
        }
        final int index = tile.getType();
        return THEME[index];
    }

    /** Clears the locally cached color theme.
     *  Call this when the user changes their theme during runtime.*/
    public static void clearTheme() {
        THEME = null;
    }
}
