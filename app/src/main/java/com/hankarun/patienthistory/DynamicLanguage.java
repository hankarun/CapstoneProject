package com.hankarun.patienthistory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by hankarun on 5.01.16.
 */
public class DynamicLanguage {
    private static final String DEFAULT = "zz";

    private Locale currentLocale;

    public void onCreate(Activity activity) {
        currentLocale = getSelectedLocale(activity);
        setContextLocale(activity, currentLocale);
    }

    public void onResume(Activity activity) {
        if (!currentLocale.equals(getSelectedLocale(activity))) {
            Intent intent = activity.getIntent();
            activity.finish();
            OverridePendingTransition.invoke(activity);
            activity.startActivity(intent);
            OverridePendingTransition.invoke(activity);
        }
    }

    private static final class OverridePendingTransition {
        static void invoke(Activity activity) {
            activity.overridePendingTransition(0, 0);
        }
    }


    private static void setContextLocale(Context context, Locale selectedLocale) {
        Configuration configuration = context.getResources().getConfiguration();

        if (!configuration.locale.equals(selectedLocale)) {
            configuration.locale = selectedLocale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLayoutDirection(selectedLocale);
            }
            context.getResources().updateConfiguration(configuration,
                    context.getResources().getDisplayMetrics());
        }
    }

    private static Locale getSelectedLocale(Context context) {
        String language[] = TextUtils.split(AppPreferences.getLanguage(context), "_");

        if (language[0].equals(DEFAULT)) {
            return Locale.getDefault();
        } else if (language.length == 2) {
            return new Locale(language[0], language[1]);
        } else {
            return new Locale(language[0]);
        }
    }
}
