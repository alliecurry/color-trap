package co.starsky.colortrap.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;
import co.starsky.colortrap.R;

/**
 * Overlays a color over the ImageView. Uses the filterColor XML attribute
 * or defaults to the theme brand color (opacity 20).
 * Color can be changed at any time by the standard use of {@link #setColorFilter(int, PorterDuff.Mode)}
 * @author alliecurry
 */
public class FilteredImageView extends ImageView {

    public FilteredImageView(Context context) {
        this(context, null);
    }

    public FilteredImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilteredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Get the theme default color
        final int defaultColor = Color.WHITE;

        // Check for a user-defined color
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FilteredImageView);
        final int color = a.getColor(R.styleable.FilteredImageView_filterColor, defaultColor);
        final String filterType = a.getString(R.styleable.FilteredImageView_filterType);
        a.recycle();

        setColorFilter(color, filterType == null ? PorterDuff.Mode.SRC_ATOP : PorterDuff.Mode.valueOf(filterType));
    }

}
