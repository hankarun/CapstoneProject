package com.hankarun.patienthistory.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.QuestionAdapter;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;


public class GroupQuestionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private Group mGroup;
    private RecyclerView mRecyclerView;
    private ArrayList<Question> mQuestionsList;
    private QuestionAdapter adapter;

    public GroupQuestionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mGroup = getArguments().getParcelable("group");

        View rootView = inflater.inflate(R.layout.fragment_group_questions, container, false);

        TextView t = (TextView) rootView.findViewById(R.id.userTextview);
        t.setText(mGroup.getmGText());

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.questionRecyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mQuestionsList = new ArrayList<>();
        adapter = new QuestionAdapter(mQuestionsList);
        mRecyclerView.setAdapter(adapter);

        if (savedInstanceState != null && !savedInstanceState.isEmpty() ) {
            mGroup = savedInstanceState.getParcelable("group");
            Parcelable[] question = savedInstanceState.getParcelableArray("questions");
            for (Parcelable aQuestion : question) mQuestionsList.add((Question) aQuestion);
            adapter.notifyDataSetChanged();
        } else {
            populateList();
        }

        return rootView;
    }

    public ArrayList<Question> getmQuestionsList(){
        return mQuestionsList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("group", mGroup);
        Question[] q = new Question[mQuestionsList.size()];
        for(int x = 0; x < mQuestionsList.size();x++)
                q[x] = mQuestionsList.get(x);
            outState.putParcelableArray("questions", q);
        super.onSaveInstanceState(outState);
    }

    public void populateList() {
        Bundle b = new Bundle();
        b.putString("test", "test");
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
        //Parse questions from cursor
        data.moveToFirst();
        while(!data.isAfterLast()){
            mQuestionsList.add(new Question(data));
            data.moveToNext();
        }
        data.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mQuestionsList.clear();
    }


}
