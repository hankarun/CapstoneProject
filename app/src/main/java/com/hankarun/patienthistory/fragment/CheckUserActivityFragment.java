package com.hankarun.patienthistory.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hankarun.patienthistory.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CheckUserActivityFragment extends Fragment {

    public CheckUserActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_check_user, container, false);
    }
}
