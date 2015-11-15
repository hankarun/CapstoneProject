package com.hankarun.patienthistory.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.fragment.PatientDetailFragment;
import com.hankarun.patienthistory.model.Answer;
import com.hankarun.patienthistory.model.Patient;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.ArrayList;

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

        ((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).populateList(patient);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton print = (FloatingActionButton) findViewById(R.id.printFAB);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }


    private ShareActionProvider mShareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store ShareActionProvider
        /*
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
        try{
            FileWriter fw = new FileWriter(vcfFile);
            fw.write("BEGIN:VCARD\r\n");
            fw.write("VERSION:3.0\r\n");
            fw.write("N:" + patient.getmName() + ";" + patient.getmSurname() + "\r\n");
            fw.write("FN:" + patient.getmName() + " " + patient.getmSurname() + "\r\n");
            fw.write("ORG:" + getApplicationContext().getResources().getString(R.string.patient) + "\r\n");
            fw.write("TITLE:" +"" + "\r\n");
            fw.write("TEL;TYPE=WORK,VOICE:" + patient.getmTelephone2() + "\r\n");
            fw.write("TEL;TYPE=HOME,VOICE:" + patient.getmTelephone1() + "\r\n");
            fw.write("ADR;TYPE=WORK:;;" +  patient.getmTown() + ";" + patient.getmCity() +  "\r\n");
            fw.write("EMAIL;TYPE=PREF,INTERNET:" + patient.getmEmail() + "\r\n");
            fw.write("END:VCARD\r\n");
            fw.close();
        }catch (Exception e){
            Log.d("vcf problem",e.getMessage());
        }

        Intent i = new Intent();
        i.setAction(android.content.Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(vcfFile), "text/x-vcard");

        mShareActionProvider.setShareIntent(i);
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

    private void print() {
        /*((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).getmAnswerList();

        /*
        File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        */
        try {
            createPdf();
        } catch (Exception e) {
            Log.d("e", e.getMessage());
        }


        Intent printIntent = new Intent(this, PrintDialog.class);
        //File root = android.os.Environment.getExternalStorageDirectory();

        File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);*/

        printIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
        printIntent.putExtra("title", "Hasta");
        startActivity(printIntent);
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        OutputStream output = new FileOutputStream(file);

        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        document.add(writeUser(patient));
        //document.add(writeQuestions(
        //        ((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).getmAnswerList()
        //));
        document.close();
    }

    private PdfPTable writeUser(Patient patient) {
        PdfPTable table = new PdfPTable(2);
        // the cell object
        PdfPCell cell;
        // we add a cell with colspan 3
        cell = new PdfPCell(new Phrase("Kisisel Bilgiler"));
        cell.setColspan(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);

        // we add the four remaining cells with addCell()
        table.addCell("İsim: " + patient.getmName() + " " + patient.getmSurname());
        table.addCell("Doğum Tarihi: " + patient.getmBirthDate());

        cell = new PdfPCell(new Phrase("Adres: " + patient.getmAddress() + " " + patient.getmTown() + " / "+patient.getmCity()));
        cell.setColspan(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        table.addCell("E-Posta: " + patient.getmEmail());
        table.addCell("Ev Tel: " + patient.getmTelephone1());
        table.addCell("Is Tel: " + patient.getmTelephone2());
        table.addCell("GSM: " + patient.getmTelephone1());

        table.addCell("Surekli Tıp Doktorunuz: " + patient.getmDoctorName() + " Tel: " + patient.getmDoctorNumber());
        table.addCell("Son Muayene Tarihi: " + patient.getmDoctorDate());
        cell = new PdfPCell(new Phrase("Kilinige gelis sebebiniz?"));
        cell.setColspan(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase(patient.getmProblems()));
        cell.setColspan(2);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
        return table;
    }

    private PdfPTable writeQuestions(ArrayList<Answer> answerArrayList) {
        return null;
    }
}
