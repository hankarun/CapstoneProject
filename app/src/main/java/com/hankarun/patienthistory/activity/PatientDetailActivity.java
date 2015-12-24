package com.hankarun.patienthistory.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hankarun.patienthistory.PrintService;
import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.PatientDetailFragment;
import com.hankarun.patienthistory.model.Patient;

import java.io.File;
import java.io.FileWriter;

public class PatientDetailActivity extends AppCompatActivity {
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent e = getIntent();
        patient = e.getParcelableExtra("patient");

        getSupportActionBar().setTitle(patient.getmName() + " " + patient.getmSurname());

        ((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).populateList(patient);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        ResponseReceiver receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);

        FloatingActionButton print = (FloatingActionButton) findViewById(R.id.printFAB);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }


    //private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        /*
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
            try {
                FileWriter fw = new FileWriter(vcfFile);
                fw.write("BEGIN:VCARD\r\n");
                fw.write("VERSION:3.0\r\n");
                fw.write("N:" + patient.getmName() + ";" + patient.getmSurname() + "\r\n");
                fw.write("FN:" + patient.getmName() + " " + patient.getmSurname() + "\r\n");
                fw.write("ORG:" + getApplicationContext().getResources().getString(R.string.patient) + "\r\n");
                fw.write("TITLE:" + "" + "\r\n");
                fw.write("TEL;TYPE=WORK,VOICE:" + patient.getmTelephone2() + "\r\n");
                fw.write("TEL;TYPE=HOME,VOICE:" + patient.getmTelephone1() + "\r\n");
                fw.write("ADR;TYPE=WORK:;;" + patient.getmTown() + ";" + patient.getmCity() + "\r\n");
                fw.write("EMAIL;TYPE=PREF,INTERNET:" + patient.getmEmail() + "\r\n");
                fw.write("END:VCARD\r\n");
                fw.close();
            } catch (Exception e) {
                Log.d("vcf problem", e.getMessage());
            }

            Intent i = new Intent();
            i.setAction(android.content.Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(vcfFile), "text/x-vcard");
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private final Activity mActiviy = this;
    private void print() {
        PatientDetailFragment f = ((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail));



        /*
        File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        */

        Intent msgIntent = new Intent(this, PrintService.class);
        msgIntent.putExtra(PrintService.USER_ID, patient.getmId());
        msgIntent.putExtra(PrintService.SURVEY_DATE,
                f.longDates.get(f.dateSpinner.getSelectedItemPosition()));
        startService(msgIntent);

        //new LongOperation().execute("", null, null);

    }


    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "com.mamlambo.intent.action.MESSAGE_PROCESSED";

        @Override
        public void onReceive(Context context, Intent intent) {
            final Intent printIntent = new Intent(mActiviy, PrintDialog.class);
            final File root = android.os.Environment.getExternalStorageDirectory();
            final File file = new File(root.getAbsolutePath(), "/Download/temp.pdf");


            AlertDialog.Builder builder = new AlertDialog.Builder(mActiviy);
            builder.setMessage(getString(R.string.what_do_you_want))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.show_file), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(intent);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(getString(R.string.print_file), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            printIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
                            printIntent.putExtra("title", getString(R.string.patient));
                            startActivity(printIntent);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();        }
    }
}
