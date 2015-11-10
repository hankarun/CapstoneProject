package com.hankarun.patienthistory.fragment;


import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.Switch;
import android.widget.Toast;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.helper.AdapterDataUpdateInterface;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.DialogInterface;
import com.hankarun.patienthistory.helper.GroupEditDialog;
import com.hankarun.patienthistory.helper.ObjectListAdapter;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.helper.listdraghelper.OnStartDragListener;
import com.hankarun.patienthistory.helper.listdraghelper.SimpleItemTouchHelperCallback;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;


public class GroupListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>,OnStartDragListener,AdapterDataUpdateInterface,DialogInterface {
    private ArrayList<Object> mGroups;
    private ObjectListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;


    public GroupListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_group_list, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.groupListRecyclerView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mGroups = new ArrayList<>();

        mAdapter = new ObjectListAdapter(getActivity(), mGroups, this ,this,rootView);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        populateList();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            openDialog(new Group());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateList() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                QuesSQLiteHelper.GROUP_TABLE_ID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT,
                QuesSQLiteHelper.GROUP_TABLE_DETAIL};
        return new CursorLoader(getActivity(), DataContentProvider.CONTENT_URI_GROUPS, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            do{
                mGroups.add(new Group(data));
            }while(data.moveToNext());
        }
        //data.close();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGroups.clear();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    //This is the interface return from adapter. For onclick and order
    @Override
    public void updateDataBase(int type, Object object) {
        Group group = (Group) object;
        switch (type){
            case ObjectListAdapter.UPDATE_OBJECT:
                openDialog(group);
                break;
            case ObjectListAdapter.DELETE_OBJECT:
                //TODO delete
                break;
            case ObjectListAdapter.ADD_OBJECT:
                //TODO add group.
                break;
        }
    }

    private void openDialog(Group group){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        GroupEditDialog newFragment = GroupEditDialog.newInstance(group);
        newFragment.setListener(this);
        newFragment.show(ft, "dialog");
    }

    //This is the interface return from dialog.
    @Override
    public void dialogCompleted(Object group) {
        Group gRoup = (Group) group;
        if(gRoup.getmId()!=0) {
            //TODO update database
            Uri todoUri = Uri.parse(DataContentProvider.CONTENT_URI_GROUPS + "/" + gRoup.getmId());
            getActivity().getContentResolver().update(todoUri, ((Group) group).getContentValues(), null, null);
            mGroups.clear();
            mAdapter.notifyDataSetChanged();
            populateList();
        } else {
            getActivity().getContentResolver().insert(DataContentProvider.CONTENT_URI_GROUPS, ((Group) group).getContentValues());
            mGroups.clear();
            mAdapter.notifyDataSetChanged();
            populateList();

        }
        //TODO Insert database
    }
}
