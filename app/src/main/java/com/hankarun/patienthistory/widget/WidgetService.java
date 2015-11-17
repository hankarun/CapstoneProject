package com.hankarun.patienthistory.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.RemoteViewsService;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.model.Patient;

public class WidgetService extends RemoteViewsService implements Loader.OnLoadCompleteListener<Cursor> {
    private WidgetDataProvider dataProvider;

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
        dataProvider.entities.clear();
        data.moveToFirst();
        while(!data.isAfterLast()){
            dataProvider.entities.add(new Patient(data));
            data.moveToNext();
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(getApplication(), WidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetCollectionList);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        dataProvider = new WidgetDataProvider(
                getApplicationContext(), intent);

        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.PATIENT_NAME,
                PatientSQLiteHelper.PATIENT_SURNAME,
                PatientSQLiteHelper.PATIENT_BDATE,
                PatientSQLiteHelper.PATIENT_TEL1,
                PatientSQLiteHelper.PATIENT_TEL2,
                PatientSQLiteHelper.PATIENT_ADDRESS,
                PatientSQLiteHelper.PATIENT_CITY,
                PatientSQLiteHelper.PATIENT_TOWN,
                PatientSQLiteHelper.PATIENT_EMAIL,
                PatientSQLiteHelper.PATIENT_DOCTOR_NAME,
                PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER,
                PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS};
        CursorLoader mCursorLoader = new CursorLoader(getApplicationContext(),
                DataContentProvider.CONTENT_URI_PATIENT, projection, null, null, null);
        mCursorLoader.registerListener(0, this);
        mCursorLoader.startLoading();
        return dataProvider;
    }
}
