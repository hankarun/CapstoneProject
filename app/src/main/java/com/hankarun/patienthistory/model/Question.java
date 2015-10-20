package com.hankarun.patienthistory.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hankarun.patienthistory.helper.QuesSQLiteHelper;

public class Question implements Parcelable{
    private int mId;
    private String mQuestion;
    private int mType;
    private int mGroupId;

    public Question(Cursor cursor){
        setmId(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_ID)));
        setmQuestion(cursor.getString(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TEXT)));
        setmType(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TYPE)));
        setmGroupId(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_GROUPID)));
    }

    public void setmId(int id){ mId = id;}
    public void setmQuestion(String question) { mQuestion = question;}
    public void setmType(int type) { mType = type;}
    public void setmGroupId(int groupId) { mGroupId = groupId;}

    public int getmId() { return mId;}
    public String getmQuestion() { return mQuestion;}
    public int getmType() { return mType;}
    public int getmGroupId() { return mGroupId;}

    public String toString(){
        return "Id = " + mId + " q = " + mQuestion + " t = " + mType + " gid = " + mGroupId;
    }

    //Parcable
    public Question(Parcel parcel){
        mQuestion = parcel.readString();
        int[] info = new int[2];
        parcel.readIntArray(info);
        mId = info[0];
        mType = info[1];
        mGroupId = info[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {mId, mType, mGroupId});
        dest.writeString(mQuestion);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
