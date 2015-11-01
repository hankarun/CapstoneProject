package com.hankarun.patienthistory.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hankarun.patienthistory.helper.PatientSQLiteHelper;

import java.util.Date;

public class Answer implements Parcelable{
    private int mId;
    private int mUserId;
    private int mQuestionId;
    private Boolean mAnswer;
    private String mDetail;
    private String mDate;
    private String mQuestion;

    public void setmId(int id) { mId = id;}
    public void setmAnswer(Boolean answer) { mAnswer = answer;}
    public void setmUserId(int userid) { mUserId = userid;}
    public void setmQuestionId(int questionId) { mQuestionId = questionId;}
    public void setmDetail(String detail){ mDetail = detail;}
    public void setmDate(String date){ mDate = date;}
    public void setmQuestion(String question){ mQuestion = question;}

    public int getmId(){ return mId;}
    public int getmUserId(){ return mUserId;}
    public int getmQuestionId(){ return mQuestionId;}
    public Boolean getmAnswer() { return mAnswer;}
    public String getmDetail(){ return mDetail;}
    public String getmDate(){ return mDate;}
    public String getmQuestion(){ return mQuestion;}


    public Answer(String question){
        mQuestion = question;
        mAnswer = false;
    }

    public Answer(Question question){
        mAnswer = question.getmAnswer();
        mQuestion = question.getmQuestion();
    }

    public Answer(Cursor cursor){
        setmId(cursor.getInt(cursor.getColumnIndex(PatientSQLiteHelper.COLUMN_ID)));
        setmUserId(cursor.getInt(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER_PATIENT_ID)));
        setmQuestionId(cursor.getInt(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION_ID)));
        setmAnswer((cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER)).equals("1")));
        setmDetail(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER_DETAIL)));
        setmDate(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER_DATE)));
        setmQuestion(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.ANSWER_QUESTION)));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        //values.put(PatientSQLiteHelper.COLUMN_ID, getmId());
        values.put(PatientSQLiteHelper.ANSWER, getmAnswer()?"1":"0");
        values.put(PatientSQLiteHelper.ANSWER_DETAIL, getmDetail());
        values.put(PatientSQLiteHelper.ANSWER_PATIENT_ID, getmQuestionId());
        values.put(PatientSQLiteHelper.ANSWER_DATE, getmDate());
        values.put(PatientSQLiteHelper.ANSWER_PATIENT_ID, getmUserId());
        values.put(PatientSQLiteHelper.ANSWER_QUESTION, getmQuestion());
        return values;
    }

    public Answer(Parcel parcel){
        boolean[] answer = new boolean[1];
        parcel.readBooleanArray(answer);
        mAnswer = answer[0];
        setmId(parcel.readInt());
        setmUserId(parcel.readInt());
        setmQuestionId(parcel.readInt());
        setmDetail(parcel.readString());
        setmDate(parcel.readString());
        setmQuestion(parcel.readString());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[]{getmAnswer()});
        dest.writeInt(getmId());
        dest.writeInt(getmUserId());
        dest.writeInt(getmQuestionId());
        dest.writeString(getmDetail());
        dest.writeString(getmDate());
        dest.writeString(getmQuestion());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Answer createFromParcel(Parcel in) {
            return new Answer(in);
        }

        public Answer[] newArray(int size) {
            return new Answer[size];
        }
    };
}
