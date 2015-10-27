package com.hankarun.patienthistory.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hankarun.patienthistory.helper.PatientSQLiteHelper;

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

    public int getmId() { return mId;}
    public String getmName() { return mName;}
    public String getmSurname() { return mSurname;}
    public Date getmBirthDate() { return mBirthDate;}
    public String getmAddress() { return mAddress;}
    public String getmEmail() { return mEmail;}
    public String getmTelephone1() { return mTelephone1;}
    public String getmTelephone2() { return mTelephone2;}
    public String getmDoctorName() { return mDoctorName;}
    public String getmDoctorNumber() { return mDoctorNumber;}
    public Date getmDoctorDate() { return mDoctorDate;}
    public String mProblems() { return mProblems;}

    public void setmId(int id){ mId = id;}
    public void setmName(String name){ mName = name;}
    public void setmSurname(String surname){ mSurname = surname;}
    public void setmBirthDate(Date birthdate){ mBirthDate = birthdate;}
    public void setmAddress(String address){ mAddress = address;}
    public void setmEmail(String email){ mEmail = email;}
    public void setmTelephone1(String telephone){ mTelephone1 = telephone;}
    public void setmTelephone2(String telephone){ mTelephone2 = telephone;}
    public void setmDoctorName(String name){ mDoctorName = name;}
    public void setmDoctorNumber(String number){ mDoctorNumber = number;}
    public void setmDoctorDate(Date date){ mDoctorDate = date;}
    public void setmProblems(String problems){ mProblems = problems;}


    public void addAnswer(Answer answer){ mAnswers.add(answer);}
    public ArrayList<Answer> getmAnswers(){ return mAnswers;}

    public Patient(){
        mAnswers = new ArrayList<>();
    }

    public Patient(Cursor cursor){

    }

    public Patient(Parcel parcel){

    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();

        values.put(PatientSQLiteHelper.PATIENT_NAME, mName);
        values.put(PatientSQLiteHelper.PATIENT_SURNAME, mSurname);

        return values;
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
