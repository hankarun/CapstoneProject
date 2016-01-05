package com.hankarun.patienthistory;

import android.content.Context;
import android.preference.PreferenceManager;

public class AppPreferences {
    public static final String LANGUAGE_PREF = "pref_language";


    public static String getLanguage(Context context) {
        return getStringPreference(context, LANGUAGE_PREF, "tr");
    }

    public static void setLanguage(Context context, String language) {
        setStringPreference(context, LANGUAGE_PREF, language);
    }

    public static void setStringPreference(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
    }

    public static String getStringPreference(Context context, String key, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }
}
