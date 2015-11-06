package com.hankarun.patienthistory.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientAdapter;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;


public class PatientListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
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

        adapter = new PatientAdapter(getActivity(),mPatientList);
        mRecyclerView.setAdapter(adapter);

        populateList();
        return rootView;
    }

    public void populateList() {
        Bundle b = new Bundle();
        b.putString("test", "test");
        getLoaderManager().initLoader(0, b, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.PATIENT_NAME,
                PatientSQLiteHelper.PATIENT_SURNAME,
                PatientSQLiteHelper.PATIENT_BDATE,
                PatientSQLiteHelper.PATIENT_TEL1,
                PatientSQLiteHelper.PATIENT_TEL2,
                PatientSQLiteHelper.PATIENT_ADDRESS,
                PatientSQLiteHelper.PATIENT_EMAIL,
                PatientSQLiteHelper.PATIENT_DOCTOR_NAME,
                PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER,
                PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS};
        CursorLoader cursorLoader = new CursorLoader(getContext(),
                DataContentProvider.CONTENT_URI_PATIENT, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()){
            do{
                mPatientList.add(new Patient(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPatientList.clear();
    }

    //TODO Add state saving.
}
