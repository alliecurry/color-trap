package co.starsky.colortrap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import co.starsky.colortrap.R;
import co.starsky.colortrap.util.FontUtility;

/**
 * TextView subclass which uses custom fonts.
 * @author alliecurry
 */
public class FontyTextView extends TextView {
    private static final String TAG = FontyTextView.class.getSimpleName();

    public FontyTextView(Context context) {
        super(context);
    }

    public FontyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context context, AttributeSet attrs) {
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.FontyTextView);
        String customFont = attrArray.getString(R.styleable.FontyTextView_customFont);
        assignFont(context, this, customFont);
        attrArray.recycle();
    }

    /** Sets the font face to the specified asset name. Font name must include file extension. */
    public static boolean assignFont(Context context, TextView textView, String fontName) {
        Typeface typeface = null;

        try {
            typeface = FontUtility.get(context, fontName);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }

        textView.setTypeface(typeface);
        return true;
    }

}
