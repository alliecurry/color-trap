package co.starsky.colortrap.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static String FIRST_PLAY = "fp";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("hscprefs", 0);
    }

    public static boolean isFirstPlay(Context context) {
        return getPrefs(context).getBoolean(FIRST_PLAY, true);
    }

    public static void setFirstPlay(Context context) {
        getPrefs(context).edit().putBoolean(FIRST_PLAY, false).commit();
    }
}
