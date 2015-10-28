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

    public void setmId(int id) { mId = id;}
    public void setmAnswer(Boolean answer) { mAnswer = answer;}
    public void setmUserId(int userid) { mUserId = userid;}
    public void setmQuestionId(int questionId) { mQuestionId = questionId;}
    public void setmDetail(String detail){ mDetail = detail;}
    public void setmDate(String date){ mDate = date;}

    public int getmId(){ return mId;}
    public int getmUserId(){ return mUserId;}
    public int getmQuestionId(){ return mQuestionId;}
    public Boolean getmAnswer() { return mAnswer;}
    public String getmDetail(){ return mDetail;}
    public String getmDate(){ return mDate;}


    public Answer(int questionId){
        mQuestionId = questionId;
        mAnswer = false;
    }

    public Answer(Question question){
        mAnswer = question.getmAnswer();
        mQuestionId = question.getmId();
    }

    public Answer(Cursor cursor){
        //TODO: Cursor to Answer implementation
        setmId(cursor.getInt(cursor.getColumnIndex(PatientSQLiteHelper.COLUMN_ID)));

    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        //values.put(PatientSQLiteHelper.COLUMN_ID, getmId());
        values.put(PatientSQLiteHelper.ANSWER, getmAnswer());
        values.put(PatientSQLiteHelper.ANSWER_DETAIL, getmDetail());
        values.put(PatientSQLiteHelper.ANSWER_PATIENT_ID, getmQuestionId());
        values.put(PatientSQLiteHelper.ANSWER_DATE, getmDate());
        values.put(PatientSQLiteHelper.ANSWER_PATIENT_ID, getmUserId());
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
        //TODO Add date objects.
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
