package co.starsky.colortrap;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * @author alliecurry
 */
public class CTApplication extends Application {
    private Tracker tracker;

    /** @return Google Analytics Tracker. */
    public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            if (BuildConfig.DEBUG) {
                analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            }
            tracker = analytics.newTracker(getString(R.string.ga_trackingId));
        }
        return tracker;
    }

}
