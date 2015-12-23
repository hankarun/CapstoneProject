package com.hankarun.patienthistory.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.hankarun.patienthistory.PrintService;
import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.FinishFragment;
import com.hankarun.patienthistory.fragment.GroupQuestionsFragment;
import com.hankarun.patienthistory.fragment.UserEntryFragment;
import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.NonSwipebleViewPager;
import com.hankarun.patienthistory.helper.QuesSQLiteHelper;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Patient;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class QuestionsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private NonSwipebleViewPager viewPager;
    private ArrayList<Group> mGroups;
    private int currentpage = 0;
    private FloatingActionButton left;
    private FloatingActionButton right;

    private InterstitialAd mInterstitialAd;
    private Patient mPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questions);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mGroups = new ArrayList<>();

        Intent b = getIntent();

        mPatient = new Patient();
        if(b.hasExtra("patient"))
            mPatient = b.getParcelableExtra("patient");


        viewPager = (NonSwipebleViewPager) findViewById(R.id.viewpager);

        if (savedInstanceState == null || !savedInstanceState.containsKey("key")) {
            Log.d("returned", "Activity");
            loadGroups();
        } else {
            mGroups = savedInstanceState.getParcelableArrayList("key");
            setup();
        }

        left = (FloatingActionButton) findViewById(R.id.left);
        right = (FloatingActionButton) findViewById(R.id.right);

        left.setOnClickListener(this);
        right.setOnClickListener(this);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                finish();
            }
        });

        requestNewInterstitial();


        Calendar c = Calendar.getInstance();
        int start = c.get(Calendar.MILLISECOND);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private UserEntryFragment uFragment;
    private ArrayList<GroupQuestionsFragment> mQuList;

    private void setup() {
        if(mPatient.getmId()!=0)
            uFragment = UserEntryFragment.newInstance(mPatient);
        else
            uFragment = new UserEntryFragment();
        mQuList = new ArrayList<>();
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(uFragment, "user");
        int x = 1;
        for (Group g : mGroups) {
            GroupQuestionsFragment fragment = new GroupQuestionsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("group", g);
            fragment.setArguments(bundle);
            mQuList.add(fragment);
            adapter.addFrag(fragment, x + "");
            x = x + 1;
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
        return new CursorLoader(this,
                DataContentProvider.CONTENT_URI_GROUPS, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            do {
                mGroups.add(new Group(data));
            } while (data.moveToNext());
        }
        data.close();
        setup();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                viewPager.setCurrentItem(currentpage - 1);
                break;
            case R.id.right:
                if (currentpage == 0) {
                    if (uFragment.checkInput())
                        viewPager.setCurrentItem(currentpage + 1);
                } else {
                    viewPager.setCurrentItem(currentpage + 1);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (currentpage == 0) {
            Toast.makeText(getApplicationContext(), "All progress cleared.", Toast.LENGTH_SHORT).show();
            Intent a = new Intent(this,GreetingActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        viewPager.setCurrentItem(currentpage - 1);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public final List<Fragment> mFragmentList = new ArrayList<>();
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

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", mGroups);
        super.onSaveInstanceState(outState);
    }

    private Uri writePatientToDatabase(Patient patient) {
        ContentValues values = patient.toContentValues();
        return getContentResolver().insert(DataContentProvider.CONTENT_URI_PATIENT, values);
    }

    private int writeAnswerToDatabase(ArrayList<ContentValues> contentValues) {
        ContentValues values[] = new ContentValues[contentValues.size()];
        contentValues.toArray(values);
        return getContentResolver().bulkInsert(DataContentProvider.CONTENT_URI_ANSWERS, values);
    }

    //Collect data from fragments. Create user get id from uri add answers based on patient.
    public void getInfo() {
        Patient p = uFragment.getPatient();
        if(p.getmId()==0)
            p.setmId(Integer.parseInt(writePatientToDatabase(p).getLastPathSegment()));

        ArrayList<ContentValues> contentValues = new ArrayList<>();
        Date date = new Date();
        final long tmie = date.getTime();


        for (GroupQuestionsFragment f : mQuList) {
            ArrayList<Question> q = f.getmQuestionsList();
            //Get answers and add to the user
            for (Question question : q) {
                Answer a = new Answer(question);
                a.setmUserId(p.getmId());
                a.setmDate(tmie + "");
                a.setmQuestionGroup(mGroups.get(question.getmGroupId() - 1).getmGText());
                a.setmQuestionType(question.getmType());
                a.setmDetail(question.getmAnswerDetail());
                contentValues.add(a.toContentValues());

            }
        }
        writeAnswerToDatabase(contentValues);

        Intent msgIntent = new Intent(this, PrintService.class);
        msgIntent.putExtra(PrintService.USER_ID, p.getmId());
        msgIntent.putExtra(PrintService.SURVEY_DATE, tmie);
        msgIntent.putExtra(PrintService.PRINT_AFTER, true);
        startService(msgIntent);

        Intent a = new Intent(this,GreetingActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }
}
