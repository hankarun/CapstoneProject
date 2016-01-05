package com.hankarun.patienthistory;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

public class BaseAppCompatActivity extends AppCompatActivity {

    private static final String LOCALE_EXTRA = "LOCALE_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onPrecreate();
        super.onCreate(savedInstanceState);
    }

    protected void onPrecreate(){}

    protected <T extends Fragment> T initFragment(@IdRes int target,
                                                  @NonNull T fragment,
                                                  @Nullable Locale locale,
                                                  @Nullable Bundle extras)
    {
        Bundle args = new Bundle();
        args.putSerializable(LOCALE_EXTRA, locale);

        if (extras != null) {
            args.putAll(extras);
        }

        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(target, fragment)
                .commit();
        return fragment;
    }
}
