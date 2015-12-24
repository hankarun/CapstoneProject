package com.hankarun.patienthistory.fragment;


import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private ArrayList<Question> mQuestionsList;
    private QuestionAdapter adapter;

    private static final String TAG = "GroupQuestionsFragment";

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

        TextView a = (TextView) rootView.findViewById(R.id.textView21);
        a.setText(mGroup.getmGDetail());

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.questionRecyclerView);

        Configuration config = getActivity().getResources().getConfiguration();

        if((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)== Configuration.SCREENLAYOUT_SIZE_XLARGE
                && config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        mQuestionsList = new ArrayList<>();
        adapter = new QuestionAdapter(mQuestionsList);
        mRecyclerView.setAdapter(adapter);

        if (savedInstanceState != null && !savedInstanceState.isEmpty() ) {
            mGroup = savedInstanceState.getParcelable("group");

            Parcelable[] question = savedInstanceState.getParcelableArray("questions");
            for (Parcelable aQuestion : question) mQuestionsList.add((Question) aQuestion);
            Log.d("returned", "fragment " + mQuestionsList.size());
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
        Log.d("saved","fragment " + mQuestionsList.size());
        outState.putParcelable("group", mGroup);
        Question[] q = new Question[mQuestionsList.size()];
        for(int x = 0; x < mQuestionsList.size();x++)
                q[x] = mQuestionsList.get(x);
            outState.putParcelableArray("questions", q);
        super.onSaveInstanceState(outState);
    }

    private void populateList() {
        Bundle b = new Bundle();
        b.putString("test", "test");
        getLoaderManager().initLoader(mGroup.getmId(), b, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                QuesSQLiteHelper.TABLE_QUESTIONS+"."+QuesSQLiteHelper.QUESTION_TABLE_ID,
                QuesSQLiteHelper.QUESTION_TABLE_TEXT,
                QuesSQLiteHelper.QUESTION_TABLE_TYPE,
                QuesSQLiteHelper.QUESTION_TABLE_GROUPID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT};
        Uri uri = Uri.parse(DataContentProvider.CONTENT_URI_GROUPS + "/" + id);
        return new CursorLoader(getActivity(),
                uri, projection, null, null, null);
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
