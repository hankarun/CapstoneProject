package com.hankarun.patienthistory.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.activity.QuestionsActivity;

public class GreetingActivityFragment extends Fragment implements Button.OnClickListener {

    //private SimpleCursorAdapter dataAdapter;

    public GreetingActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_greeting, container, false);
        Button button = (Button) rootView.findViewById(R.id.startButton);
        button.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), QuestionsActivity.class);
        startActivity(intent);
    }

    /*public void populateList1(){
        String[] columns = new String[] {
                QuesSQLiteHelper.GROUP_TABLE_ID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT,
                QuesSQLiteHelper.GROUP_TABLE_DETAIL
        };

        int[] to = new int[] {
                R.id.textView,
                R.id.textView2,
                R.id.textView3,
        };

        dataAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.item,
                null,
                columns,
                to,
                0);

        list.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        getLoaderManager().initLoader(0, null, this);
    }

    public void populateList(){
        String[] columns = new String[] {
                QuesSQLiteHelper.QUESTION_TABLE_ID,
                QuesSQLiteHelper.QUESTION_TABLE_TEXT,
                QuesSQLiteHelper.QUESTION_TABLE_GROUPID
        };

        int[] to = new int[] {
                R.id.textView,
                R.id.textView2,
                R.id.textView3,
        };

        dataAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.item,
                null,
                columns,
                to,
                0);

        list.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        Bundle b = new Bundle();
        b.putString("test","test");
        getLoaderManager().initLoader(5, b, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(args.getString("test"),"-");
        /*String[] projection = {
                QuesSQLiteHelper.GROUP_TABLE_ID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT,
                QuesSQLiteHelper.GROUP_TABLE_DETAIL};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DataContentProvider.CONTENT_URI2, projection, null, null, null);
        String[] projection = {
                QuesSQLiteHelper.QUESTION_TABLE_ID,
                QuesSQLiteHelper.QUESTION_TABLE_TEXT,
                QuesSQLiteHelper.QUESTION_TABLE_TYPE,
                QuesSQLiteHelper.QUESTION_TABLE_GROUPID};
        Uri uri = Uri.parse(DataContentProvider.CONTENT_URI2 + "/" + id);
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                uri, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }*/
}
