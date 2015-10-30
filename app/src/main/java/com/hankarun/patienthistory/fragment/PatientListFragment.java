package com.hankarun.patienthistory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.PatientAdapter;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PatientListFragment extends Fragment {
    private ArrayList<Patient> mPatientList;
    private RecyclerView mRecyclerView;
    private PatientAdapter adapter;

    public PatientListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_patient_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.patientListRecyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mPatientList = new ArrayList<>();
        adapter = new PatientAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        return rootView;
    }
}
