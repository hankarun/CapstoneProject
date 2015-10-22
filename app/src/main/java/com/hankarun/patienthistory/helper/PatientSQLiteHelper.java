package com.hankarun.patienthistory.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PatientSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "patient";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PATIENTS = "patients";
    public static final String TABLE_ANSWERS = "answers";

    public static final String QUESTION_TABLE_ID = "_id";
    public static final String QUESTION_TABLE_TEXT = "qtext";
    public static final String QUESTION_TABLE_TYPE = "qtype";
    public static final String QUESTION_TABLE_GROUPID = "qgroup_id";

    public static final String GROUP_TABLE_ID = "_id";
    public static final String GROUP_TABLE_TEXT = "gtext";
    public static final String GROUP_TABLE_DETAIL = "gdetail";

    private Context mContext;

    public PatientSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
