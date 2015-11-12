package com.hankarun.patienthistory;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.widget.RemoteViewsService;

import com.hankarun.patienthistory.model.Patient;

public class WidgetService extends RemoteViewsService implements Loader.OnLoadCompleteListener<Cursor> {
    WidgetDataProvider dataProvider;


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
        //Load cursor
        return null;
    }
}
