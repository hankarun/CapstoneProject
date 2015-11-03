package com.hankarun.patienthistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Patient;


public class UserEntryFragment extends Fragment {
    private EditText nameTxt;
    private EditText surnameTxt;
    private EditText birtText;
    private EditText emailText;
    private EditText tel1Text;
    private EditText tel2Text;
    private EditText addressText;

    public UserEntryFragment() {
    }

    private Patient patient;

    @Override
    public void onResume() {
        Log.d("userFragment", "resumed");
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        patient = new Patient();
        View rootView = inflater.inflate(R.layout.fragment_user_entry, container, false);


        nameTxt = (EditText) rootView.findViewById(R.id.input_name);
        surnameTxt = (EditText) rootView.findViewById(R.id.input_surname);
        birtText = (EditText) rootView.findViewById(R.id.input_bdate);
        emailText = (EditText) rootView.findViewById(R.id.input_email);
        tel1Text = (EditText) rootView.findViewById(R.id.input_tel1);
        tel2Text = (EditText) rootView.findViewById(R.id.input_tel2);
        addressText = (EditText) rootView.findViewById(R.id.input_adress);

        if(savedInstanceState != null){
            patient = savedInstanceState.getParcelable("patient");

            nameTxt.setText(patient.getmName());
            surnameTxt.setText(patient.getmSurname());
            birtText.setText(patient.getmBirthDate());
            emailText.setText(patient.getmEmail());
            tel1Text.setText(patient.getmTelephone1());
            tel2Text.setText(patient.getmTelephone2());
            addressText.setText(patient.getmAddress());
        }


        return rootView;
    }

    public Patient getPatient(){
        if(nameTxt == null){
            return patient;
        }else {
            Patient patient = new Patient();

            patient.setmName(nameTxt.getText().toString());
            patient.setmSurname(surnameTxt.getText().toString());
            patient.setmBirthDate(birtText.getText().toString());
            patient.setmEmail(emailText.getText().toString());
            patient.setmTelephone1(tel1Text.getText().toString());
            patient.setmTelephone2(tel2Text.getText().toString());
            patient.setmAddress(addressText.getText().toString());

            return patient;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        patient.setmName(nameTxt.getText().toString());
        patient.setmSurname(surnameTxt.getText().toString());
        patient.setmBirthDate(birtText.getText().toString());
        patient.setmEmail(emailText.getText().toString());
        patient.setmTelephone1(tel1Text.getText().toString());
        patient.setmTelephone2(tel2Text.getText().toString());
        patient.setmAddress(addressText.getText().toString());
        outState.putParcelable("patient",patient);
        super.onSaveInstanceState(outState);
    }
}
