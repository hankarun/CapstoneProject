package com.hankarun.patienthistory.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

public class Patient implements Parcelable{
    private int mId;
    private String mName;
    private String mSurname;
    private Date mBirthDate;
    private String mAddress;
    private String mEmail;
    private String mTelephone1;
    private String mTelephone2;
    private String mDoctorName;
    private String mDoctorNumber;
    private Date mDoctorDate;
    private String mProblems;

    private ArrayList<Answer> mAnswers;



    public Patient(Cursor cursor){

    }

    public Patient(Parcel parcel){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
