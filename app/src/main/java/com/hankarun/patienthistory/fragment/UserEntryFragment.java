package com.hankarun.patienthistory.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Patient;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserEntryFragment extends Fragment {
    private EditText nameTxt;
    private EditText surnameTxt;
    private EditText birtText;
    private EditText emailText;
    private EditText tel1Text;
    private EditText tel2Text;
    private EditText addressText;
    private AutoCompleteTextView addressTown;
    private AutoCompleteTextView addressCity;
    private EditText doctorName;
    private EditText doctorTel;
    private EditText docdate;
    private EditText docProblem;


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
        tel1Text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        tel2Text = (EditText) rootView.findViewById(R.id.input_tel2);
        tel2Text.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        addressText = (EditText) rootView.findViewById(R.id.input_adress);

        addressTown = (AutoCompleteTextView) rootView.findViewById(R.id.town_input);
        addressCity = (AutoCompleteTextView) rootView.findViewById(R.id.city_input);
        doctorName = (EditText) rootView.findViewById(R.id.doctor_name_input);
        docProblem = (EditText) rootView.findViewById(R.id.problem_input);
        docdate = (EditText) rootView.findViewById(R.id.doc_date_input);

        doctorTel = (EditText) rootView.findViewById(R.id.doc_tel_input);
        doctorTel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        String[] iller = getResources().getStringArray(R.array.iller);
        String[] ilceler = getResources().getStringArray(R.array.ilce);
        ArrayAdapter<String> ilceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ilceler);
        ArrayAdapter<String> illerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, iller);

        addressCity.setAdapter(illerAdapter);
        addressTown.setAdapter(ilceAdapter);

        if (savedInstanceState != null) {
            patient = savedInstanceState.getParcelable("patient");

            nameTxt.setText(patient.getmName());
            surnameTxt.setText(patient.getmSurname());
            birtText.setText(patient.getmBirthDate());
            emailText.setText(patient.getmEmail());
            tel1Text.setText(patient.getmTelephone1());
            tel2Text.setText(patient.getmTelephone2());
            addressText.setText(patient.getmAddress());
            addressCity.setText(patient.getmCity());
            addressTown.setText(patient.getmTown());
            doctorName.setText(patient.getmDoctorName());
            docdate.setText(patient.getmDoctorDate());
            docProblem.setText(patient.getmProblems());
            doctorTel.setText(patient.getmDoctorNumber());
        }

        docdate.addTextChangedListener(new MyTextWatcher(docdate));
        birtText.addTextChangedListener(new MyTextWatcher(birtText));

        return rootView;
    }

    public class MyTextWatcher implements TextWatcher{
        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private EditText myEdit;

        public MyTextWatcher(EditText edit){
            myEdit = edit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!s.toString().equals(current)) {
                String clean = s.toString().replaceAll("[^\\d.]", "");
                String cleanC = current.replaceAll("[^\\d.]", "");

                int cl = clean.length();
                int sel = cl;
                for (int i = 2; i <= cl && i < 6; i += 2) {
                    sel++;
                }
                //Fix for pressing delete next to a forward slash
                if (clean.equals(cleanC)) sel--;

                if (clean.length() < 8) {
                    clean = clean + ddmmyyyy.substring(clean.length());
                } else {
                    //This part makes sure that when we finish entering numbers
                    //the date is correct, fixing it otherwise
                    int day = Integer.parseInt(clean.substring(0, 2));
                    int mon = Integer.parseInt(clean.substring(2, 4));
                    int year = Integer.parseInt(clean.substring(4, 8));

                    if (mon > 12) mon = 12;
                    cal.set(Calendar.MONTH, mon - 1);
                    year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                    cal.set(Calendar.YEAR, year);
                    // ^ first set year for the line below to work correctly
                    //with leap years - otherwise, date e.g. 29/02/2012
                    //would be automatically corrected to 28/02/2012

                    day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                    clean = String.format("%02d%02d%02d", day, mon, year);
                }

                clean = String.format("%s/%s/%s", clean.substring(0, 2),
                        clean.substring(2, 4),
                        clean.substring(4, 8));

                sel = sel < 0 ? 0 : sel;
                current = clean;
                myEdit.setText(current);
                myEdit.setSelection(sel < current.length() ? sel : current.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    public Patient getPatient() {
        if (nameTxt == null) {
            return patient;
        } else {
            return getPatientFromViews();
        }
    }

    public Patient getPatientFromViews(){
        Patient patient = new Patient();
        patient.setmName(nameTxt.getText().toString());
        patient.setmSurname(surnameTxt.getText().toString());
        patient.setmBirthDate(birtText.getText().toString());
        patient.setmEmail(emailText.getText().toString());
        patient.setmTelephone1(tel1Text.getText().toString());
        patient.setmTelephone2(tel2Text.getText().toString());
        patient.setmAddress(addressText.getText().toString());
        patient.setmCity(addressCity.getText().toString());
        patient.setmTown(addressTown.getText().toString());
        patient.setmDoctorName(doctorName.getText().toString());
        patient.setmDoctorNumber(doctorTel.getText().toString());
        patient.setmDoctorDate(docdate.getText().toString());
        patient.setmProblems(docProblem.getText().toString());
        return patient;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("patient", getPatientFromViews());
        super.onSaveInstanceState(outState);
    }
}
