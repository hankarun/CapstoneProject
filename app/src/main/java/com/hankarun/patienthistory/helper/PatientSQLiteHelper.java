package com.hankarun.patienthistory.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;

public class PatientSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "patient.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PATIENTS = "patients";
    public static final String TABLE_ANSWERS = "answers";

    public static final String COLUMN_ID = "_id";

    public static final String PATIENT_NAME = "name";
    public static final String PATIENT_SURNAME = "surname";
    public static final String PATIENT_BDATE = "bdate";
    public static final String PATIENT_EMAIL = "email";
    public static final String PATIENT_ADDRESS = "address";
    public static final String PATIENT_TEL1 = "tel1";
    public static final String PATIENT_TEL2 = "tel2";
    public static final String PATIENT_DOCTOR_NAME = "dname";
    public static final String PATIENT_DOCTOR_NUMBER = "dnumber";
    public static final String PATIENT_DOCTOR_DATE = "ddate";
    public static final String PATIENT_DOCTOR_PROBLEMS = "dproblems";

    public static final String ANSWER_TEXT = "atext";
    public static final String ANSWER_PATIENT_ID = "pid";
    public static final String ANSWER_DETAIL = "adetail";
    public static final String ANSWER_DATE = "adate";



    private static final String DATABASE_CREATE_PATIENTS = "create table "
            + TABLE_PATIENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + PATIENT_NAME + " text, "
            + PATIENT_SURNAME + " text, "
            + PATIENT_BDATE + " date, "
            + PATIENT_EMAIL + " text, "
            + PATIENT_ADDRESS + " text, "
            + PATIENT_TEL1 + " text, "
            + PATIENT_TEL2 + " text, "
            + PATIENT_DOCTOR_NAME + " text, "
            + PATIENT_DOCTOR_NUMBER + " text, "
            + PATIENT_DOCTOR_DATE + " date, "
            + PATIENT_DOCTOR_PROBLEMS + " text"
            +");";

    private static final String DATABASE_CREATE_ANSWERS = "create table "
            + TABLE_ANSWERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + ANSWER_PATIENT_ID + " intenger, "
            + ANSWER_TEXT + " text, "
            + ANSWER_DETAIL + " text, "
            + ANSWER_DATE + " date"
            + ");";

    private Context mContext;

    public PatientSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_PATIENTS);
        db.execSQL(DATABASE_CREATE_ANSWERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        onCreate(db);
    }
}
