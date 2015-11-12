package com.hankarun.patienthistory.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.PatientDetailFragment;
import com.hankarun.patienthistory.model.Patient;

import java.io.File;

public class PatientDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent e = getIntent();
        Patient patient = e.getParcelableExtra("patient");

        ((PatientDetailFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).populateList(patient);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton print = (FloatingActionButton) findViewById(R.id.printFAB);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

    private void print(){
        Intent printIntent = new Intent(this, PrintDialog.class);
        File file = new File("/sdcard/StudentLatePass.txt");
        printIntent.setDataAndType(Uri.fromFile(file), "text/*");
        printIntent.putExtra("title", "Hasta");
        startActivity(printIntent);
    }

}
