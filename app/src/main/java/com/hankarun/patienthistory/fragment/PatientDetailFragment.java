package com.hankarun.patienthistory.fragment;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientDetailAdapter;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;


public class PatientDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private TextView nameTextView;
    private TextView birthTextView;
    private TextView emailTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private TextView phoneNumber2TextView;
    private TextView doctorNameTextView;
    private TextView doctorTelTextView;
    private TextView doctorDateTextView;
    private TextView problemTextView;

    private ArrayList<Answer> mAnswerList;
    private PatientDetailAdapter adapter;


    public PatientDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_detail, container, false);


        nameTextView = (TextView) v.findViewById(R.id.patientNameSurnameText);
        birthTextView = (TextView) v.findViewById(R.id.patientBirthDateText);
        emailTextView = (TextView) v.findViewById(R.id.patientEmailText);
        addressTextView = (TextView) v.findViewById(R.id.patientAddress);
        phoneNumberTextView = (TextView) v.findViewById(R.id.patientTel1Text);
        phoneNumber2TextView = (TextView) v.findViewById(R.id.patientTel2Text);
        doctorNameTextView = (TextView) v.findViewById(R.id.patientDoctorName);
        doctorTelTextView = (TextView) v.findViewById(R.id.patientDoctorTel);
        doctorDateTextView = (TextView) v.findViewById(R.id.patientDoctorDate);
        problemTextView = (TextView) v.findViewById(R.id.patientProblem);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.answersRecyclerView);

        Configuration config = getActivity().getResources().getConfiguration();


        if((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)== Configuration.SCREENLAYOUT_SIZE_XLARGE
                && config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL,false));
        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        mAnswerList = new ArrayList<>();
        adapter = new PatientDetailAdapter(getContext(), mAnswerList);
        mRecyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Patient patient = bundle.getParcelable("patient");
            if (patient != null) {
                populateList(patient);
            }
        }

        return v;
    }

    public void populateList(Patient patient){
        String twmp = patient.getmName()+" "+patient.getmSurname();
        nameTextView.setText(twmp);
        birthTextView.setText(patient.getmBirthDate());
        emailTextView.setText(patient.getmEmail());
        phoneNumberTextView.setText(patient.getmTelephone1());
        phoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumber2TextView.setText(patient.getmTelephone2());
        phoneNumber2TextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        String temp = patient.getmAddress()+" " + patient.getmTown() + " - " + patient.getmCity();
        addressTextView.setText(temp);
        doctorNameTextView.setText(patient.getmDoctorName());
        doctorTelTextView.setText(patient.getmDoctorNumber());
        doctorDateTextView.setText(patient.getmDoctorDate());
        problemTextView.setText(patient.getmProblems());
        
        getLoaderManager().initLoader(patient.getmId(), null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.ANSWER,
                PatientSQLiteHelper.ANSWER_DATE,
                PatientSQLiteHelper.ANSWER_QUESTION_ID,
                PatientSQLiteHelper.ANSWER_DETAIL,
                PatientSQLiteHelper.ANSWER_PATIENT_ID,
                PatientSQLiteHelper.ANSWER_QUESTION};
        Uri uri = Uri.parse(DataContentProvider.CONTENT_URI_ANSWERS + "/" + id);
        return new CursorLoader(getActivity(),
                uri, projection, null, null, null);    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()){
            do{
                mAnswerList.add(new Answer(data));
            }while(data.moveToNext());
        }
        //data.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAnswerList.clear();
    }

}
