package com.hankarun.patienthistory.model;


import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Answer implements Parcelable{
    private int mId;
    private int mUserId;
    private String mText;
    private String mDetail;
    private Date mDate;

    public Answer(Cursor cursor){

    }

    public Answer(Parcel parcel){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

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
