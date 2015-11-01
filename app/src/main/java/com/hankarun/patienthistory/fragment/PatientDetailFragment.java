package com.hankarun.patienthistory.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;
import java.util.List;


public class PatientDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private LinearLayout mLinearLayout;
    private TextView nameTextView;
    private TextView surnameTextView;
    private TextView birthTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private TextView phoneNumber2TextView;

    private ListView mAnswerListView;

    private ArrayList<Answer> mAnswerList;
    private MyAdapter adapter;


    public PatientDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_patient_detail, container, false);

        mLinearLayout = (LinearLayout) v.findViewById(R.id.patientItem);
        nameTextView = (TextView) v.findViewById(R.id.patientNameText);
        surnameTextView = (TextView) v.findViewById(R.id.patientSurnameText);
        birthTextView = (TextView) v.findViewById(R.id.patientBirthDateText);
        emailTextView = (TextView) v.findViewById(R.id.patientEmailText);
        phoneNumberTextView = (TextView) v.findViewById(R.id.patientNumberText);
        phoneNumber2TextView = (TextView) v.findViewById(R.id.patientNumber2);

        mAnswerListView = (ListView) v.findViewById(R.id.answersListView);

        mAnswerList = new ArrayList<>();
        adapter = new MyAdapter(getContext(), R.layout.item, mAnswerList);
        mAnswerListView.setAdapter(adapter);

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

    public class MyAdapter extends ArrayAdapter<Answer> {
        Context context;
        ArrayList<Answer> mAnswers;
        int layoutResource;

        public MyAdapter(Context context, int resource, List<Answer> objects) {
            super(context, resource, objects);

            mAnswers = (ArrayList<Answer>) objects;
            this.context = context;
            layoutResource = resource;
        }

        @Override
        public int getCount() {
            return mAnswers.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(layoutResource, parent, false);
            }

            Answer answer = mAnswers.get(position);

            TextView textView = (TextView) convertView.findViewById(R.id.textView2);
            String a = answer.getmQuestion();
            a = a + (answer.getmAnswer() ? "Evet" : "Hayır");
            textView.setText(a);

            return convertView;
        }
    }

}
