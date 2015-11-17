package com.hankarun.patienthistory.helper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class QuesSQLiteHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "data.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_QUESTIONS = "questions";
    public static final String TABLE_GROUPS = "groups";

    public static final String QUESTION_TABLE_ID = "_id";
    public static final String QUESTION_TABLE_TEXT = "qtext";
    public static final String QUESTION_TABLE_TYPE = "qtype";
    public static final String QUESTION_TABLE_GROUPID = "qgroup_id";

    public static final String GROUP_TABLE_ID = "_id";
    public static final String GROUP_TABLE_TEXT = "gtext";
    public static final String GROUP_TABLE_DETAIL = "gdetail";

    private SQLiteDatabase myDataBase;


    private Context context;

    public QuesSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(){
        boolean dbExist = checkDataBase();

        if(!dbExist){
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = context.getFilesDir().getParentFile().getPath() + "/databases/" + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            Log.d("", e.getMessage());
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = context.getFilesDir().getParentFile().getPath()+ "/databases/" + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase(){
        String myPath = context.getFilesDir().getParentFile().getPath()+ "/databases/" + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
