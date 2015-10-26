package com.hankarun.patienthistory.model;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Answer implements Parcelable{
    private int mId;
    private int mUserId;
    private int mQuestionId;
    private Boolean mAnswer;
    private String mDetail;
    private Date mDate;

    public void setmAnswer(Boolean answer) { mAnswer = answer;}

    public Answer(int questionId){
        mQuestionId = questionId;
        mAnswer = false;
    }

    public Answer(Question question){
        mAnswer = question.getmAnswer();
        mQuestionId = question.getmId();
    }

    public Answer(Cursor cursor){

    }

    public Answer(Parcel parcel){
        boolean[] answer = new boolean[1];
        parcel.readBooleanArray(answer);
        mAnswer = answer[0];
    }

    public Boolean getmAnswer() { return mAnswer;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray(new boolean[] {mAnswer});
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
