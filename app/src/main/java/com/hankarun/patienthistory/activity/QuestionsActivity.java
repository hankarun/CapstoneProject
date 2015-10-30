package com.hankarun.patienthistory.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hankarun.patienthistory.fragment.FinishFragment;
import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.GroupQuestionsFragment;
import com.hankarun.patienthistory.fragment.UserEntryFragment;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Patient;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;
import java.util.List;


public class QuestionsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private ViewPager viewPager;
    private ArrayList<Group> mGroups;
    private int currentpage = 0;
    private FloatingActionButton left;
    private FloatingActionButton right;

    public Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questions);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mGroups = new ArrayList<>();
        mPatient = new Patient();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        if(savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            loadGroups();
        }else{
            mGroups = savedInstanceState.getParcelableArrayList("key");
            setup();
        }

        left = (FloatingActionButton) findViewById(R.id.left);
        right = (FloatingActionButton) findViewById(R.id.right);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

    }

    private UserEntryFragment uFragment;
    private ArrayList<GroupQuestionsFragment> mQuList;

    private void setup(){
        uFragment = new UserEntryFragment();
        mQuList = new ArrayList<>();
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(uFragment, "user");
        for(Group g:mGroups){
            GroupQuestionsFragment fragment = new GroupQuestionsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("group",g);
            fragment.setArguments(bundle);
            mQuList.add(fragment);
            adapter.addFrag(fragment, "group");
        }
        adapter.addFrag(new FinishFragment(), "end");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    left.hide();
                else
                    left.show();
                if (position == adapter.getCount() - 1)
                    right.hide();
                else
                    right.show();
                currentpage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                DataContentProvider.CONTENT_URI_GROUPS, projection, null, null, null);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left:
                viewPager.setCurrentItem(currentpage-1);
                break;
            case R.id.right:
                viewPager.setCurrentItem(currentpage+1);
                break;
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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

    public Uri writePatientToDatabase(Patient patient) {
        ContentValues values = patient.toContentValues();
        return getContentResolver().insert(DataContentProvider.CONTENT_URI_PATIENT, values);
    }

    public Uri writeAnswerToDatabase(Answer answer){
        ContentValues values = answer.toContentValues();
        return getContentResolver().insert(DataContentProvider.CONTENT_URI_ANSWERS,values);
    }

    //Collect data from fragments. Create user get id from uri add answers based on patient.
    public void getInfo(){
        Patient p = uFragment.getPatient();

        //Patient id provided. Add them to answers.
        Log.d("patient ","created " + writePatientToDatabase(p).getLastPathSegment());

        p.setmId(Integer.parseInt(writePatientToDatabase(p).getLastPathSegment()));

        for(GroupQuestionsFragment f:mQuList){
            ArrayList<Question> q = f.getmQuestionsList();
            //Get answers and add to the user
            for(Question question:q){
                Answer a = new Answer(question);
                a.setmUserId(p.getmId());

                writeAnswerToDatabase(a);
                //User id and other fields.
            }
        }

        finish();
    }
}
