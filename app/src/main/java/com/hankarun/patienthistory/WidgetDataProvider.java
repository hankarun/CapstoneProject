package com.hankarun.patienthistory;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hankarun.patienthistory.model.Patient;

import java.util.ArrayList;

/**
 * Created by hankarun on 12.11.15.
 */
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
        /*RemoteViews mView = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);

        mView.setTextViewText(R.id.homeNameView,entities.get(i).getHomeName());
        mView.setTextViewText(R.id.awayNameView,entities.get(i).getAvayName());
        mView.setTextViewText(R.id.homeScoreView,entities.get(i).getHomeScore());
        mView.setTextViewText(R.id.awayScoreView,entities.get(i).getAvayScore());
            */
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
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
