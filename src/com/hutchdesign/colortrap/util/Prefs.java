package com.hutchdesign.colortrap.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private static String SESSION_PREF = "cansessionexist";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("hscprefs", 0);
    }

    public static boolean getSessionPref(Context context) {
        return getPrefs(context).getBoolean(SESSION_PREF, false);
    }

    public static void setSessionPref(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(SESSION_PREF, value).commit();
    }
}
