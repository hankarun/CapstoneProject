package com.hankarun.patienthistory.helper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLException;

public class DataContentProvider extends ContentProvider {
    private static final String PROVIDER_NAME = "com.hankarun.patienthistory";

    private QuesSQLiteHelper mQuestionDatabase;

    public static final Uri CONTENT_URI1 =
            Uri.parse("content://"+ PROVIDER_NAME + "/questions");
    public static final Uri CONTENT_URI2 =
            Uri.parse("content://"+ PROVIDER_NAME + "/groups");

    private static final int QUESTION = 10;
    private static final int QUESTION_ID = 11;
    private static final int GROUP = 20;
    private static final int GROUP_ID = 22;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "questions", QUESTION);
        uriMatcher.addURI(PROVIDER_NAME, "questions/#", QUESTION_ID);
        uriMatcher.addURI(PROVIDER_NAME, "groups", GROUP);
        uriMatcher.addURI(PROVIDER_NAME, "groups/#", GROUP_ID);
    }

    @Override
    public boolean onCreate() {
        mQuestionDatabase = new QuesSQLiteHelper(getContext());
        try {
            mQuestionDatabase.onCreate();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            mQuestionDatabase.openDataBase();
        }catch(SQLException sqle){

        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        Log.d("uri ", uriMatcher.match(uri)+"");
        switch (uriMatcher.match(uri)){
            case QUESTION:
                //all questions
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS);
                break;
            case QUESTION_ID:
                //O soruyu getir. uri.getLastPathSegment()
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS);
                queryBuilder.appendWhere(QuesSQLiteHelper.QUESTION_TABLE_ID + "=" + uri.getLastPathSegment());
                break;
            case GROUP:
                //Tüm grupları gönder
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_GROUPS);
                break;
            case GROUP_ID:
                queryBuilder.setTables(QuesSQLiteHelper.TABLE_QUESTIONS);
                queryBuilder.appendWhere(QuesSQLiteHelper.QUESTION_TABLE_GROUPID + "=" + uri.getLastPathSegment());
                //Group id ye göre soruları gönder. uri.getLastPathSegment()
                break;
        }

        SQLiteDatabase db = mQuestionDatabase.getWritableDatabase();
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
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
