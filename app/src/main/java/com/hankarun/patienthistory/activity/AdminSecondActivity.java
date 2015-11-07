package com.hankarun.patienthistory.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.GroupListFragment;
import com.hankarun.patienthistory.fragment.PatientListFragment;

public class AdminSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        setDrawer(intent.getIntExtra("fragment",0));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setDrawer(int position){
        Fragment fragment = null;
        Class fragmentClass = null;
        String fragmentName = null;
        //Check other fragment if active

        switch (position) {
            case 0:
                //Open patients activity or fragment
                fragmentClass = PatientListFragment.class;
                fragmentName = "patient";
                break;
            case 1:
                //Open groups activity or fragment
                fragmentClass = GroupListFragment.class;
                fragmentName = "groups";
                break;
            case 2:
                //Open questions activity or fragment
                break;
            case 3:
                //Open settings activity or fragment
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.d("eroor", e.toString());
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container_body, fragment, fragmentName).commit();
    }

}
