package com.hankarun.patienthistory.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    Context mContext = null;
    ArrayList<Patient> entities = new ArrayList<>();

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return entities.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.item);

        Log.d("name", entities.get(position).getmName());

        mView.setTextViewText(R.id.questionTextView, entities.get(position).getmName() + " " + entities.get(position).getmSurname());
        mView.setTextViewText(R.id.answerTextView, entities.get(position).getmTelephone1());

        final Intent fillInIntent = new Intent();
        fillInIntent.setAction("test");
        final Bundle bundle = new Bundle();
        bundle.putString("position", entities.get(position).getmTelephone1());
        fillInIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.answerLayout, fillInIntent);


        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
