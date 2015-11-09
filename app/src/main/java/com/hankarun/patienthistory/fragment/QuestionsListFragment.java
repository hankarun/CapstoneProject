package com.hankarun.patienthistory.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.AdapterDataUpdateInterface;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.DialogInterface;
import com.hankarun.patienthistory.helper.ObjectListAdapter;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.helper.QuestionEditDialog;
import com.hankarun.patienthistory.helper.listdraghelper.OnStartDragListener;
import com.hankarun.patienthistory.helper.listdraghelper.SimpleItemTouchHelperCallback;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;

public class QuestionsListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, OnStartDragListener, AdapterDataUpdateInterface, DialogInterface {
    private ArrayList<ArrayList<Object>> mQuestionsByGroup;
    private ArrayList<Group> mGroups;
    private Spinner grouSpinner;
    private RecyclerView mRecyclerView;
    private ObjectListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ArrayList<Object> current;

    public QuestionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questions_list, container, false);

        grouSpinner = (Spinner) rootView.findViewById(R.id.groupSpinner);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.questionListRecycler);

        setupRecycler(rootView);
        loadGroups();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            openDialog(new Question());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecycler(View rootView) {

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mGroups = new ArrayList<>();
        current = new ArrayList<>();
        mAdapter = new ObjectListAdapter(getActivity(), current, this, this, rootView);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    private void setupSpinner() {

        final ArrayAdapter<Group> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mGroups);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        grouSpinner.setAdapter(adapter);

        grouSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current.clear();
                current.addAll(mQuestionsByGroup.get(position));
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadGroups() {
        mGroups = new ArrayList<>();
        mQuestionsByGroup = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                QuesSQLiteHelper.TABLE_GROUPS + "." + QuesSQLiteHelper.GROUP_TABLE_ID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT,
                QuesSQLiteHelper.GROUP_TABLE_DETAIL,
                QuesSQLiteHelper.QUESTION_TABLE_TEXT,
                QuesSQLiteHelper.TABLE_QUESTIONS + "." + QuesSQLiteHelper.QUESTION_TABLE_ID + " AS QID"};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                DataContentProvider.CONTENT_URI_GROUPS_ALL, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            do {
                Group group = new Group(data);
                if (mGroups.size() > 0) {
                    if (mGroups.get(mGroups.size() - 1).getmId() != group.getmId()) {
                        mGroups.add(group);
                        mQuestionsByGroup.add(new ArrayList<>());
                    }
                } else {
                    mGroups.add(group);
                    mQuestionsByGroup.add(new ArrayList<>());
                }
                if(data.getString(data.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TEXT))!=null) {
                    Question question = new Question();
                    question.setmQuestion(data.getString(data.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TEXT)));
                    question.setmGroupId(group.getmId());
                    question.setmId(Integer.parseInt(data.getString(data.getColumnIndex("QID"))));
                    mQuestionsByGroup.get(mQuestionsByGroup.size() - 1).add(question);
                }
            } while (data.moveToNext());
        }
        //data.close();
        setupSpinner();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    //This is the interface return from adapter.
    @Override
    public void updateDataBase(int type, Object object) {
        Question question = (Question) object;
        switch (type){
            case ObjectListAdapter.UPDATE_OBJECT:
                openDialog(question);
                break;
            case ObjectListAdapter.DELETE_OBJECT:
                //TODO delete
                break;
        }
    }

    //Show edit question dialog.
    private void openDialog(Question question){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        if(question.getmGroupId()==0)
            question.setmGroupId(grouSpinner.getSelectedItemPosition()+1);
        QuestionEditDialog newFragment = QuestionEditDialog.newInstance(question,mGroups);
        newFragment.setListener(this);
        newFragment.show(ft, "dialog");
    }

    //This is the interface return from dialog.
    @Override
    public void dialogCompleted(Object group) {
        Question question = (Question) group;

        if(question.getmId()!=0) {
            Uri todoUri = Uri.parse(DataContentProvider.CONTENT_URI_QUESTIONS + "/" + question.getmId());
            getActivity().getContentResolver().update(todoUri, question.getContentValues(), null, null);
            mGroups.clear();
            loadGroups();
        } else {
            getActivity().getContentResolver().insert(DataContentProvider.CONTENT_URI_QUESTIONS, question.getContentValues());
            mGroups.clear();
            loadGroups();
        }
    }
}
