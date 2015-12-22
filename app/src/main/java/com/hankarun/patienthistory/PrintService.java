package com.hankarun.patienthistory;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.hankarun.patienthistory.helper.DataContentProvider;
import com.hankarun.patienthistory.helper.PatientSQLiteHelper;
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
import java.io.OutputStream;
import java.util.ArrayList;


public class PrintService extends IntentService {
    private ArrayList<Answer> mAnswerList;
    private Patient mPatient;

    public PrintService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id = intent.getIntExtra("user_id", -1);
        String date = intent.getStringExtra("survey_date");

        if (id != -1) {

            retriveUser(id);

            retriveSurvey(date);

            try {
                createPdf();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    private void retriveUser(int id){
        //TODO retrive user
        String[] projection = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.PATIENT_NAME,
                PatientSQLiteHelper.PATIENT_SURNAME,
                PatientSQLiteHelper.PATIENT_BDATE,
                PatientSQLiteHelper.PATIENT_TEL1,
                PatientSQLiteHelper.PATIENT_TEL2,
                PatientSQLiteHelper.PATIENT_ADDRESS,
                PatientSQLiteHelper.PATIENT_CITY,
                PatientSQLiteHelper.PATIENT_TOWN,
                PatientSQLiteHelper.PATIENT_EMAIL,
                PatientSQLiteHelper.PATIENT_DOCTOR_NAME,
                PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER,
                PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS};

        Cursor userCursor = getContentResolver().query(DataContentProvider.CONTENT_URI_PATIENT, projection,
                PatientSQLiteHelper.COLUMN_ID + " = '" + id + "'", null, null);

        mPatient = new Patient(userCursor);
    }

    private void retriveSurvey(String date){
        //TODO retrive user answers
        String[] projection1 = {
                PatientSQLiteHelper.COLUMN_ID,
                PatientSQLiteHelper.ANSWER,
                PatientSQLiteHelper.ANSWER_DATE,
                PatientSQLiteHelper.ANSWER_QUESTION_GROUP,
                PatientSQLiteHelper.ANSWER_DETAIL,
                PatientSQLiteHelper.ANSWER_PATIENT_ID,
                PatientSQLiteHelper.ANSWER_QUESTION_GROUP,
                PatientSQLiteHelper.ANSWER_QUESTION,
                PatientSQLiteHelper.ANSWER_QUESTION_TYPE};

        Cursor data = getContentResolver().query(Uri.parse(DataContentProvider.CONTENT_URI_ANSWERS + "/" + mPatient.getmId()), projection1,
                PatientSQLiteHelper.ANSWER_DATE + " = '" + date + "'", null, null);

        mAnswerList = new ArrayList<>();
        String group = "";
        if (data.moveToFirst()) {
            do {
                if (!data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION_GROUP)).equals(group)) {
                    group = data.getString(data.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION_GROUP));
                    Answer temp = new Answer();
                    temp.setmQuestionGroup(group);
                    temp.setmId(-1);
                    temp.setmQuestionType(2);
                    mAnswerList.add(temp);
                }
                mAnswerList.add(new Answer(data));
            } while (data.moveToNext());
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File root = android.os.Environment.getExternalStorageDirectory();

        File file = new File(root.getAbsolutePath(), "/Download/temp.pdf");
        OutputStream output = new FileOutputStream(file);

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, output);
        document.open();

        document.setPageSize(PageSize.A4);
        document.setMargins(20, 20, 20, 20);
        document.setMarginMirroringTopBottom(true);

        document.add(writeUser(mPatient, document));
        writeQuestions(mAnswerList, document, writer);
        document.close();
    }

    private PdfPTable writeUser(Patient patient, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
        table.setTotalWidth(document.right() - document.left());
        table.setLockedWidth(true);
        table.setWidths(new int[]{1, 1, 1, 1});


        PdfPCell cell;
        cell = getCell(getString(R.string.personal_details), false);
        cell.setColspan(4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);

        table.addCell(getCell(getString(R.string.name), false));
        table.addCell(getCell(": " + patient.getmName() + " " + patient.getmSurname(), false));
        table.addCell(getCell(getString(R.string.birth_date), false));
        table.addCell(getCell(": " + patient.getmBirthDate(), false));
        table.addCell(getCell(getString(R.string.address), false));
        table.addCell(getCell(": " + patient.getmAddress(), false));
        table.addCell(getCell(patient.getmTown() + " / " + patient.getmCity(), false));
        table.addCell(getCell(" ", false));
        table.addCell(getCell(getString(R.string.mobile_phone), false));
        table.addCell(getCell(": " + patient.getmTelephone1(), false));
        table.addCell(getCell(getString(R.string.work_phone), false));
        table.addCell(getCell(": " + patient.getmTelephone2(), false));
        table.addCell(getCell(getString(R.string.email), false));
        table.addCell(getCell(": " + patient.getmEmail(), false));
        table.addCell(getCell(" ", false));
        table.addCell(getCell(" ", false));

        table.addCell(getCell(getString(R.string.doctors_details), false));
        table.addCell(getCell(": " + patient.getmDoctorName(), false));
        table.addCell(getCell(" - " + patient.getmDoctorNumber(), false));
        table.addCell(getCell(" - " + patient.getmDoctorDate(), false));

        table.addCell(getCell(getString(R.string.problems) + ": ", false));
        cell = getCell(patient.getmProblems(), false);
        cell.setColspan(3);
        table.addCell(cell);

        return table;
    }

    private void writeQuestions(ArrayList<Answer> answerArrayList, Document document, PdfWriter writer) {
        PdfPTable table = new PdfPTable(1);
        for (Answer answer : answerArrayList) {
            if (answer.getmId() == -1) {
                try {
                    if (writer.getVerticalPosition(true) - table.getRowHeight(0) - table.getRowHeight(1) - table.getRowHeight(2) < document.bottom()) {
                        document.newPage();
                    }
                    table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

                    table.completeRow();
                    document.add(table);
                    table = newTable(document);
                } catch (Exception e) {
                    Log.d("Table", e.getMessage());
                }
                addHeader(table, answer);
            } else {
                switch (answer.getmQuestionType()) {
                    case 1:
                        addType3Cell(table, answer);
                        Log.d("check", answer.getmQuestion());
                        break;
                    case 2:
                        addType1Cell(table, answer);
                        Log.d("check", answer.getmQuestion());

                        break;
                    case 3:
                        addType2Cell(table, answer);
                        Log.d("check", answer.getmQuestion());
                        break;
                }
            }
        }
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPTable newTable(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingAfter(0);
        table.setSpacingBefore(0);
        table.setTotalWidth(document.right() - document.left());
        table.setLockedWidth(true);
        table.setWidths(new int[]{4, 1, 4, 1});
        return table;
    }

    private void addHeader(PdfPTable table, Answer answer) {
        table.setSpacingBefore(6f);
        PdfPCell cell = getCell(answer.getmQuestionGroup(), false);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(4);

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

    private void addType3Cell(PdfPTable table, Answer answer) {
        table.addCell(getCell(answer.getmQuestion() + "\n" + answer.getmDetail(), false));
        table.addCell(getCell(answer.getmAnswer() ? getString(R.string.yes) : getString(R.string.no), false));
    }

    private void addType2Cell(PdfPTable table, Answer answer) {
        PdfPCell cell = getCell(answer.getmQuestion() + ": " + answer.getmDetail(), false);
        cell.setColspan(2);
        table.addCell(cell);
    }

    private void addType1Cell(PdfPTable table, Answer answer) {
        table.addCell(getCell(answer.getmQuestion(), true));
        table.addCell(getCell(answer.getmAnswer() ? getString(R.string.yes) : getString(R.string.no), false));
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
        if (text.equals("null") || text.equals(""))
            text = "-";
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
