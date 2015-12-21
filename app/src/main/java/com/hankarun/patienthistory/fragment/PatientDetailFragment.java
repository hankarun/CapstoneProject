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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientDetailAdapter;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Patient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PatientDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {
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
    private Spinner dateSpinner;
    private RecyclerView mRecyclerView;

    private ArrayList<Answer> mAnswerList;
    private PatientDetailAdapter adapter;
    private Patient patient;

    public ArrayList<Answer> getmAnswerList() {
        return mAnswerList;
    }

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
        dateSpinner = (Spinner) v.findViewById(R.id.spinner);
        dateSpinner.setOnItemSelectedListener(this);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.answersRecyclerView);

        Configuration config = getActivity().getResources().getConfiguration();


        if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE
                && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.isHeader(position) ? 2 : 1;
                }
            });
            mRecyclerView.setLayoutManager(layoutManager);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        mAnswerList = new ArrayList<>();
        adapter = new PatientDetailAdapter(getContext(), mAnswerList);
        mRecyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            patient = bundle.getParcelable("patient");
            if (patient != null) {
                populateList(patient);
            }
        }

        return v;
    }

    public void populateList(Patient patient) {
        this.patient = patient;
        String twmp = patient.getmName() + " " + patient.getmSurname();
        nameTextView.setText(twmp);
        birthTextView.setText(patient.getmBirthDate());
        emailTextView.setText(patient.getmEmail());
        phoneNumberTextView.setText(patient.getmTelephone1());
        phoneNumberTextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumber2TextView.setText(patient.getmTelephone2());
        phoneNumber2TextView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        String temp = patient.getmAddress() + " " + patient.getmTown() + " - " + patient.getmCity();
        addressTextView.setText(temp);
        doctorNameTextView.setText(patient.getmDoctorName());
        doctorTelTextView.setText(patient.getmDoctorNumber());
        doctorDateTextView.setText(patient.getmDoctorDate());
        problemTextView.setText(patient.getmProblems());

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.ANSWER,
                PatientSQLiteHelper.ANSWER_DATE,
                PatientSQLiteHelper.ANSWER_QUESTION_GROUP,
                PatientSQLiteHelper.ANSWER_DETAIL,
                PatientSQLiteHelper.ANSWER_PATIENT_ID,
                PatientSQLiteHelper.ANSWER_QUESTION_GROUP,
                PatientSQLiteHelper.ANSWER_QUESTION,
                PatientSQLiteHelper.ANSWER_QUESTION_TYPE};
        Log.d("id ", id+"");
        switch (id) {
            case 0:
                return new CursorLoader(getActivity(),
                        Uri.parse(DataContentProvider.CONTENT_URI_ANSWERS1 + "/" + patient.getmId()), new String[] {PatientSQLiteHelper.ANSWER_DATE}, null, null,
                        PatientSQLiteHelper.ANSWER_DATE + " DESC");
            default:
                Uri uri = Uri.parse(DataContentProvider.CONTENT_URI_ANSWERS + "/" + patient.getmId());
                Log.d("date", args.getString("date"));
                return new CursorLoader(getActivity(),
                        uri, projection,
                        PatientSQLiteHelper.ANSWER_DATE + " = '"+args.getString("date")+"'", null, null);
        }
    }

    private List<String> longDates;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case 0:
                data.moveToFirst();
                List<String> dates = new ArrayList<>();
                longDates = new ArrayList<>();
                do{
                    dates.add(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(new Date(Long.parseLong(data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_DATE))))));
                    longDates.add(data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_DATE)));
                } while (data.moveToNext());

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dates);
                dateSpinner.setAdapter(dataAdapter);

                Bundle b = new Bundle();
                b.putString("date",longDates.get(0));
                getLoaderManager().initLoader(1, b, this);
                break;
            default:
                mAnswerList.clear();
                String group = "";
                if (data.moveToFirst()) {
                    do {
                        if (!data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION_GROUP)).equals(group)) {
                            group = data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION_GROUP));
                            Answer temp = new Answer();
                            temp.setmQuestionGroup(group);
                            temp.setmId(-1);
                            temp.setmQuestionType(2);
                            mAnswerList.add(temp);
                        }
                        mAnswerList.add(new Answer(data));
                    } while (data.moveToNext());
                }
                adapter = new PatientDetailAdapter(getContext(), mAnswerList);
                mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
        mAnswerList.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Bundle b = new Bundle();
        b.putString("date", longDates.get(position));
        getLoaderManager().initLoader(position+1, b, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
