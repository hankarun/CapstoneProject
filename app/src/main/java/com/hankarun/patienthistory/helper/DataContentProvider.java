package com.hankarun.patienthistory.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.sql.SQLException;

public class DataContentProvider extends ContentProvider {
    private static final String PROVIDER_NAME = "com.hankarun.patienthistory";

    private QuesSQLiteHelper mQuestionDatabase;
    private PatientSQLiteHelper mPatientAnswerDatabase;

    public static final Uri CONTENT_URI_QUESTIONS =
            Uri.parse("content://"+ PROVIDER_NAME + "/questions");
    public static final Uri CONTENT_URI_GROUPS =
            Uri.parse("content://"+ PROVIDER_NAME + "/groups");
    public static final Uri CONTENT_URI_GROUPS_ALL =
            Uri.parse("content://"+ PROVIDER_NAME + "/allgroups");
    public static final Uri CONTENT_URI_PATIENT =
            Uri.parse("content://"+ PROVIDER_NAME + "/patients");
    public static final Uri CONTENT_URI_ANSWERS =
            Uri.parse("content://"+ PROVIDER_NAME + "/answers");

    private static final int QUESTION = 10;
    private static final int QUESTION_ID = 11;
    private static final int GROUP = 20;
    private static final int GROUP_ID = 22;
    private static final int GROUP_WITH_QUESTIONS = 23;
    private static final int PATIENTS = 30;
    private static final int PATIENT_ID = 33;
    private static final int ANSWERS = 40;
    private static final int ANSWERS_ID = 44;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "questions", QUESTION); // Return all questions.
        uriMatcher.addURI(PROVIDER_NAME, "questions/#", QUESTION_ID); // Return spesific question.
        uriMatcher.addURI(PROVIDER_NAME, "groups", GROUP); //Retrun all groups.
        uriMatcher.addURI(PROVIDER_NAME, "groups/#", GROUP_ID); //Return questions depending group id.
        uriMatcher.addURI(PROVIDER_NAME, "allgroups", GROUP_WITH_QUESTIONS); //Return groups with all questions.
        uriMatcher.addURI(PROVIDER_NAME, "patients/", PATIENTS); // Return all patients.
        uriMatcher.addURI(PROVIDER_NAME, "patients/#", PATIENT_ID); //Return patient by id
        uriMatcher.addURI(PROVIDER_NAME, "answers/", ANSWERS); //Return or put answers?
        uriMatcher.addURI(PROVIDER_NAME, "answers/#", ANSWERS_ID); //ID is for patient return answers for a patient.
    }

    @Override
    public boolean onCreate() {
        mPatientAnswerDatabase = new PatientSQLiteHelper(getContext());

        mQuestionDatabase = new QuesSQLiteHelper(getContext());
        mQuestionDatabase.onCreate();

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteDatabase db = null;
        switch (uriMatcher.match(uri)) {
            case QUESTION:
                //all questions
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS+ " LEFT OUTER JOIN " + QuesSQLiteHelper.TABLE_GROUPS + " ON " +
                        QuesSQLiteHelper.QUESTION_TABLE_GROUPID + " = " + QuesSQLiteHelper.GROUP_TABLE_ID);
                break;
            case QUESTION_ID:
                //O soruyu getir. uri.getLastPathSegment()
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS);
                queryBuilder.appendWhere(QuesSQLiteHelper.QUESTION_TABLE_ID + "=" + uri.getLastPathSegment());
                db = mQuestionDatabase.getWritableDatabase();
                break;
            case GROUP:
                //Tüm grupları gönder
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_GROUPS);
                db = mQuestionDatabase.getWritableDatabase();
                break;
            case GROUP_ID:
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS+ " LEFT OUTER JOIN " + QuesSQLiteHelper.TABLE_GROUPS + " ON " +
                        QuesSQLiteHelper.QUESTION_TABLE_GROUPID + " = " + QuesSQLiteHelper.TABLE_GROUPS+"."+QuesSQLiteHelper.GROUP_TABLE_ID);
                queryBuilder.appendWhere(QuesSQLiteHelper.QUESTION_TABLE_GROUPID + "=" + uri.getLastPathSegment());
                db = mQuestionDatabase.getWritableDatabase();
                //Group id ye göre soruları gönder. uri.getLastPathSegment()
                break;
            case GROUP_WITH_QUESTIONS:
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_GROUPS + " LEFT OUTER JOIN " + QuesSQLiteHelper.TABLE_QUESTIONS + " ON " +
                        QuesSQLiteHelper.TABLE_GROUPS + "." +QuesSQLiteHelper.GROUP_TABLE_ID + " = " + QuesSQLiteHelper.TABLE_QUESTIONS + "." + QuesSQLiteHelper.QUESTION_TABLE_GROUPID);
                sortOrder = QuesSQLiteHelper.TABLE_GROUPS + "." +QuesSQLiteHelper.GROUP_TABLE_ID + ", "
                        + QuesSQLiteHelper.TABLE_QUESTIONS + "." + QuesSQLiteHelper.QUESTION_TABLE_ID + " ASC";
                db = mQuestionDatabase.getWritableDatabase();
                break;
            case PATIENTS:
                queryBuilder.setTables(PatientSQLiteHelper.TABLE_PATIENTS);
                db = mPatientAnswerDatabase.getWritableDatabase();
                break;
            case ANSWERS_ID:
                queryBuilder.setTables(PatientSQLiteHelper.TABLE_ANSWERS);
                queryBuilder.appendWhere(PatientSQLiteHelper.ANSWER_PATIENT_ID + "=" + uri.getLastPathSegment());
                db = mPatientAnswerDatabase.getWritableDatabase();
                break;
            default:
                db = mPatientAnswerDatabase.getWritableDatabase();
        }


        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB;

        long id = 0;
        Uri uri1;
        switch (uriType) {
            case PATIENTS:
                sqlDB = mPatientAnswerDatabase.getWritableDatabase();
                id = sqlDB.insert(PatientSQLiteHelper.TABLE_PATIENTS, null, values);
                uri1 = Uri.parse(PROVIDER_NAME + "patient/" + id);
                break;
            case ANSWERS:
                sqlDB = mPatientAnswerDatabase.getWritableDatabase();
                id = sqlDB.insert(PatientSQLiteHelper.TABLE_ANSWERS, null, values);
                uri1 = Uri.parse(PROVIDER_NAME + "answer/" + id);
                break;
            case GROUP:
                sqlDB = mQuestionDatabase.getWritableDatabase();
                id = sqlDB.insert(QuesSQLiteHelper.TABLE_GROUPS, null, values);
                uri1 = Uri.parse(PROVIDER_NAME + "group/" + id);
                break;
            case QUESTION:
                sqlDB = mQuestionDatabase.getWritableDatabase();
                id = sqlDB.insert(QuesSQLiteHelper.TABLE_QUESTIONS, null, values);
                uri1 = Uri.parse(PROVIDER_NAME + "answer/" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB;

        int id = 0;
        Uri uri1;
        switch (uriType) {
            case GROUP_ID:
                sqlDB = mQuestionDatabase.getWritableDatabase();
                id = sqlDB.update(QuesSQLiteHelper.TABLE_GROUPS,
                        values,
                        QuesSQLiteHelper.GROUP_TABLE_ID + "=" + uri.getLastPathSegment(),
                        selectionArgs);
                break;
            case QUESTION_ID:
                sqlDB = mQuestionDatabase.getWritableDatabase();
                id = sqlDB.update(QuesSQLiteHelper.TABLE_QUESTIONS,
                        values,
                        QuesSQLiteHelper.QUESTION_TABLE_ID + "=" + uri.getLastPathSegment(),
                        selectionArgs);
                break;
        }
        return id;
    }
}
