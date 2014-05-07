package co.starsky.colortrap.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Utility Class used to cache TypeFace instances.
 *
 * @author alliecurry
 */
public final class FontUtility {

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    private FontUtility() {
        throw new AssertionError();
    }

    /** Retrieves the specified font from the cache. Font name must include extension. */
    public static Typeface get(Context context, String fontName) {
        synchronized (cache) {
            if (!cache.containsKey(fontName)) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                        String.format("fonts/%s", fontName)
                );
                cache.put(fontName, typeface);
            }
            return cache.get(fontName);
        }
    }

}
