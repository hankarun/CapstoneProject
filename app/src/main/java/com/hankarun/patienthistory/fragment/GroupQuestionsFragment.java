package com.hankarun.patienthistory.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Group;


public class GroupQuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    Group mGroup;
    private SimpleCursorAdapter dataAdapter;
    ListView mListView;

    public GroupQuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(savedInstanceState != null){
            mGroup = savedInstanceState.getParcelable("group");
        }

        mGroup = getArguments().getParcelable("group");

        View rootView = inflater.inflate(R.layout.fragment_group_questions, container, false);

        TextView t = (TextView) rootView.findViewById(R.id.userTextview);
        t.setText(mGroup.getmGText());

        mListView = (ListView) rootView.findViewById(R.id.listView);

        populateList();

        return rootView;
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

        mListView.setAdapter(dataAdapter);
        //Ensures a loader is initialized and active.
        Bundle b = new Bundle();
        b.putString("test","test");
        getLoaderManager().initLoader(mGroup.getmId(), b, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                QuesSQLiteHelper.QUESTION_TABLE_ID,
                QuesSQLiteHelper.QUESTION_TABLE_TEXT,
                QuesSQLiteHelper.QUESTION_TABLE_TYPE,
                QuesSQLiteHelper.QUESTION_TABLE_GROUPID};
        Uri uri = Uri.parse(DataContentProvider.CONTENT_URI_GROUPS + "/" + id);
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
    }
}
