package com.hankarun.patienthistory.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

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


    final Activity mActiviy = this;
    private void print() {
        /*((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).getmAnswerList();

        /*
        File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        */
        new LongOperation().execute("", null, null);

    }

    private PdfWriter writer;

    private class LongOperation extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                createPdf();
            } catch (Exception e) {
                Log.d("e", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            final Intent printIntent = new Intent(mActiviy, PrintDialog.class);
            final File root = android.os.Environment.getExternalStorageDirectory();
            final File file = new File(root.getAbsolutePath(), "/Download/test.pdf");


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
            alert.show();
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        //File file = new File(getApplicationContext().getFilesDir(), "test.pdf");

        File root = android.os.Environment.getExternalStorageDirectory();

        File file = new File(root.getAbsolutePath(), "/Download/test.pdf");
        OutputStream output = new FileOutputStream(file);

        Document document = new Document();
        writer = PdfWriter.getInstance(document, output);
        document.open();

        document.setPageSize(PageSize.A4);
        document.setMargins(20, 20, 20, 20);
        document.setMarginMirroringTopBottom(true);

        document.add(writeUser(patient));
        document = writeQuestions(
                ((PatientDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentPatientDetail)).getmAnswerList(), document);
        document.close();
    }

    private PdfPTable writeUser(Patient patient) {
        PdfPTable table = new PdfPTable(2);

        PdfPCell cell;
        cell = getCell(getString(R.string.personal_details), false);
        cell.setColspan(2);
        table.addCell(cell);

        table.addCell(getCell(getString(R.string.name) + ": " + patient.getmName() + " " + patient.getmSurname(), false));
        table.addCell(getCell(getString(R.string.birth_date) + ": " + patient.getmBirthDate(), false));
        cell = getCell(getString(R.string.address) + ": " + patient.getmAddress() + " " + patient.getmTown() + " / " + patient.getmCity(), false);
        cell.setColspan(2);
        table.addCell(cell);
        table.addCell(getCell(getString(R.string.email) + ": " + patient.getmEmail(), false));
        table.addCell(getCell(getString(R.string.mobile_phone) + ": " + patient.getmTelephone1(), false));
        table.addCell(getCell(getString(R.string.work_phone) + ": " + patient.getmTelephone2(), false));
        table.addCell(getCell(getString(R.string.mobile_phone) + ": " + patient.getmTelephone1(), false));
        table.addCell(getCell(getString(R.string.doctors_details) + ": " + patient.getmDoctorName() + " - " + patient.getmDoctorNumber(), false));
        table.addCell(getCell(getString(R.string.doctor_date) + ": " + patient.getmDoctorDate(), false));
        cell = getCell(getString(R.string.problems) + "? ", false);
        cell.setColspan(2);
        table.addCell(cell);
        cell = getCell(patient.getmProblems(), false);
        cell.setColspan(2);
        table.addCell(cell);

        return table;
    }

    private Document writeQuestions(ArrayList<Answer> answerArrayList, Document document) {
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell;
        for (Answer answer : answerArrayList) {
            if (answer.getmId() == -1) {
                try {
                    if (writer.getVerticalPosition(true) - table.getRowHeight(0) - table.getRowHeight(1) - table.getRowHeight(2) < document.bottom()) {
                        document.newPage();
                    }
                    document.add(table);
                    table = new PdfPTable(4);
                    table.setSpacingAfter(0);
                    table.setSpacingBefore(0);
                    table.setTotalWidth(document.right() - document.left());
                    table.setLockedWidth(true);

                    table.setWidths(new int[]{4, 1, 4, 1});

                } catch (Exception e) {
                    Log.d("Table", e.getMessage());
                }

                cell = getCell(answer.getmQuestionGroup(), false);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(4);

                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            } else {
                table.addCell(getCell(answer.getmQuestion(), true));
                table.addCell(getCell(answer.getmAnswer() ? getString(R.string.yes) : getString(R.string.no), false));
            }
        }
        return document;
    }

    private PdfPCell getCell(String text, boolean question) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "Cp857", BaseFont.EMBEDDED);
        } catch (Exception e) {
            Log.d("Font ", e.getMessage());
        }

        //Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font font = new Font(bf);
        Paragraph p = new Paragraph(text, font);
        if (question) {
            Chunk leader = new Chunk(new DottedLineSeparator());
            p.add(leader);
        }
        PdfPCell cell = new PdfPCell(p);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
