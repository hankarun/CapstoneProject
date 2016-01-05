package com.hankarun.patienthistory.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.hankarun.patienthistory.AppPreferences;
import com.hankarun.patienthistory.BaseAppCompatActivity;
import com.hankarun.patienthistory.DynamicLanguage;
import com.hankarun.patienthistory.R;

public class GreetingActivity extends BaseAppCompatActivity {
    private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

    @Override
    protected void onPrecreate() {
        dynamicLanguage.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dynamicLanguage.onResume(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        final ImageView flagimage = (ImageView) findViewById(R.id.imageView11);


        if (AppPreferences.getLanguage(getApplicationContext()).equals("tr")) {
            flagimage.setActivated(true);
        }

        flagimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppPreferences.getLanguage(getApplicationContext()).equals("tr")) {
                    AppPreferences.setLanguage(getApplicationContext(), "en");
                } else {
                    AppPreferences.setLanguage(getApplicationContext(), "tr");
                }
                flagimage.setActivated(!flagimage.isActivated());
                update();
            }
        });
    }

    private void update() {
        if (Build.VERSION.SDK_INT >= 11) recreate();
        else dynamicLanguage.onResume(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_greeting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
