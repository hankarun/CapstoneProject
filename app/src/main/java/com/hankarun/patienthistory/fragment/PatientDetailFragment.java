package com.hankarun.patienthistory.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientDetailAdapter;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;
import java.util.List;


public class PatientDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView birthTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView phoneNumber2TextView;

    private ArrayList<Answer> mAnswerList;
    private PatientDetailAdapter adapter;


    public PatientDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_detail, container, false);


        nameTextView = (TextView) v.findViewById(R.id.patientNameText);
        surnameTextView = (TextView) v.findViewById(R.id.patientSurnameText);
        birthTextView = (TextView) v.findViewById(R.id.patientBirthDateText);
        emailTextView = (TextView) v.findViewById(R.id.patientEmailText);
        phoneNumberTextView = (TextView) v.findViewById(R.id.patientNumberText);
        phoneNumber2TextView = (TextView) v.findViewById(R.id.patientNumber2);

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
        nameTextView.setText(patient.getmName());
        surnameTextView.setText(patient.getmSurname());
        birthTextView.setText(patient.getmBirthDate());
        emailTextView.setText(patient.getmEmail());
        phoneNumberTextView.setText(patient.getmTelephone1());
        phoneNumber2TextView.setText(patient.getmTelephone2());
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
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                uri, projection, null, null, null);
        return cursorLoader;    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data.moveToFirst()){
            do{
                mAnswerList.add(new Answer(data));
            }while(data.moveToNext());
        }
        data.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
