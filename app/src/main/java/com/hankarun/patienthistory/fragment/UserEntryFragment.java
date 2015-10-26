package com.hankarun.patienthistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Patient;


public class UserEntryFragment extends Fragment {
    private EditText nameTxt;

    public UserEntryFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_entry, container, false);

        nameTxt = (EditText) rootView.findViewById(R.id.input_name);
        return rootView;
    }

    public String getName(){
        return nameTxt.getText().toString();
    }


}
