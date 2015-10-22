package com.hankarun.patienthistory.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hankarun.patienthistory.FinishFragment;
import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.GroupQuestionsFragment;
import com.hankarun.patienthistory.fragment.UserEntryFragment;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Group;

import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    ViewPager viewPager;
    ArrayList<Group> mGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGroups = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            loadGroups();
        }else{
            mGroups = savedInstanceState.getParcelableArrayList("key");
            setup();
        }

    }

    private void setup(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new UserEntryFragment(), "");
        for(Group g:mGroups){
            GroupQuestionsFragment fragment = new GroupQuestionsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("group",g);
            fragment.setArguments(bundle);
            adapter.addFrag(fragment, g.getmGText());
        }
        adapter.addFrag(new FinishFragment(), "");
        viewPager.setAdapter(adapter);
    }

    private void loadGroups() {
        getSupportLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                QuesSQLiteHelper.GROUP_TABLE_ID,
                QuesSQLiteHelper.GROUP_TABLE_TEXT,
                QuesSQLiteHelper.GROUP_TABLE_DETAIL};
        CursorLoader cursorLoader = new CursorLoader(this,
                DataContentProvider.CONTENT_URI2, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()){
            do{
                mGroups.add(new Group(data));
            }while(data.moveToNext());
        }
        data.close();
        setup();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", mGroups);
        super.onSaveInstanceState(outState);
    }
}
