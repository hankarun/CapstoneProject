package com.hankarun.patienthistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.QuestionsActivity;


public class FinishFragment extends Fragment {


    public FinishFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final QuestionsActivity a = (QuestionsActivity) getActivity();
        View rootView = inflater.inflate(R.layout.fragment_finish, container, false);
        Button end = (Button) rootView.findViewById(R.id.endButton);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.getInfo();
            }
        });
        return rootView;
    }


}
