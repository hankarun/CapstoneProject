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
    private String mBirthDate;
    private String mAddress;
    private String mEmail;
    private String mTelephone1;
    private String mTelephone2;
    private String mDoctorName;
    private String mDoctorNumber;
    private Date mDoctorDate;
    private String mProblems;

    public int getmId() { return mId;}
    public String getmName() { return mName;}
    public String getmSurname() { return mSurname;}
    public String getmBirthDate() { return mBirthDate;}
    public String getmAddress() { return mAddress;}
    public String getmEmail() { return mEmail;}
    public String getmTelephone1() { return mTelephone1;}
    public String getmTelephone2() { return mTelephone2;}
    public String getmDoctorName() { return mDoctorName;}
    public String getmDoctorNumber() { return mDoctorNumber;}
    public Date getmDoctorDate() { return mDoctorDate;}
    public String getmProblems() { return mProblems;}

    public void setmId(int id){ mId = id;}
    public void setmName(String name){ mName = name;}
    public void setmSurname(String surname){ mSurname = surname;}
    public void setmBirthDate(String birthdate){ mBirthDate = birthdate;}
    public void setmAddress(String address){ mAddress = address;}
    public void setmEmail(String email){ mEmail = email;}
    public void setmTelephone1(String telephone){ mTelephone1 = telephone;}
    public void setmTelephone2(String telephone){ mTelephone2 = telephone;}
    public void setmDoctorName(String name){ mDoctorName = name;}
    public void setmDoctorNumber(String number){ mDoctorNumber = number;}
    public void setmDoctorDate(Date date){ mDoctorDate = date;}
    public void setmProblems(String problems){ mProblems = problems;}



    public Patient(){

    }

    public Patient(Cursor cursor){
        setmId(cursor.getInt(cursor.getColumnIndex(PatientSQLiteHelper.COLUMN_ID)));
        setmName(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_NAME)));
        setmSurname(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_SURNAME)));
        setmBirthDate(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_BDATE)));
        setmAddress(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_ADDRESS)));
        setmEmail(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_EMAIL)));
        setmTelephone1(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_TEL1)));
        setmTelephone2(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_TEL2)));
        setmDoctorName(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_DOCTOR_NAME)));
        setmDoctorNumber(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER)));

        //setmDoctorDate(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_SURNAME)));

        setmProblems(cursor.getString(cursor.getColumnIndex(PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS)));
    }

    public Patient(Parcel parcel){
        //TODO: Add date values
        setmId(parcel.readInt());
        setmName(parcel.readString());
        setmSurname(parcel.readString());
        setmAddress(parcel.readString());
        setmBirthDate(parcel.readString());
        setmEmail(parcel.readString());
        setmTelephone1(parcel.readString());
        setmTelephone2(parcel.readString());
        setmDoctorName(parcel.readString());
        setmDoctorNumber(parcel.readString());
        setmProblems(parcel.readString());
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        //TODO: Add date values
        values.put(PatientSQLiteHelper.PATIENT_NAME, mName);
        values.put(PatientSQLiteHelper.PATIENT_SURNAME, mSurname);
        values.put(PatientSQLiteHelper.PATIENT_ADDRESS, mAddress);
        values.put(PatientSQLiteHelper.PATIENT_BDATE,mBirthDate);
        values.put(PatientSQLiteHelper.PATIENT_EMAIL, mEmail);
        values.put(PatientSQLiteHelper.PATIENT_TEL1, mTelephone1);
        values.put(PatientSQLiteHelper.PATIENT_TEL2, mTelephone2);
        values.put(PatientSQLiteHelper.PATIENT_DOCTOR_NAME, mDoctorName);
        values.put(PatientSQLiteHelper.PATIENT_DOCTOR_NUMBER, mDoctorNumber);
        values.put(PatientSQLiteHelper.PATIENT_DOCTOR_PROBLEMS, mProblems);

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //TODO: Add date values
        dest.writeInt(getmId());
        dest.writeString(getmName());
        dest.writeString(getmSurname());
        dest.writeString(getmAddress());
        dest.writeString(getmBirthDate());
        dest.writeString(getmEmail());
        dest.writeString(getmTelephone1());
        dest.writeString(getmTelephone2());
        dest.writeString(getmDoctorName());
        dest.writeString(getmDoctorNumber());
        dest.writeString(getmProblems());
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
