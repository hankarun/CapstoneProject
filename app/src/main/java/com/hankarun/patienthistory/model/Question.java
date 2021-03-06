package com.hankarun.patienthistory.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hankarun.patienthistory.helper.QuesSQLiteHelper;

public class Question implements Parcelable{
    private int mId;
    private String mQuestion;
    private int mType;
    private int mGroupId;
    private boolean mAnswer;
    private String mAnswerDetail;

    public Question(){

    }

    public Question(Cursor cursor){
        setmId(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_ID)));
        setmQuestion(cursor.getString(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TEXT)));
        setmType(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_TYPE)));
        setmGroupId(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.QUESTION_TABLE_GROUPID)));
        mAnswer = false;
    }

    public void setmId(int id){ mId = id;}
    public void setmQuestion(String question) { mQuestion = question;}
    public void setmType(int type) { mType = type;}
    public void setmGroupId(int groupId) { mGroupId = groupId;}
    public void setmAnswer(boolean answer) { mAnswer = answer;}
    public void setmAnswerDetail(String detail) { mAnswerDetail = detail;}

    public int getmId() { return mId;}
    public String getmQuestion() { return mQuestion;}
    public int getmType() { return mType;}
    public int getmGroupId() { return mGroupId;}
    public boolean getmAnswer() { return mAnswer;}
    public String getmAnswerDetail() { return mAnswerDetail;}

    public String toString(){
        return mQuestion;
    }

    //Parcable
    public Question(Parcel parcel){
        mId = parcel.readInt();
        mType = parcel.readInt();
        mGroupId = parcel.readInt();
        int te = parcel.readInt();
        mAnswer = te == 1;
        mQuestion = parcel.readString();
        mAnswerDetail = parcel.readString();
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        if(getmId()!=0)
            values.put(QuesSQLiteHelper.QUESTION_TABLE_ID,getmId());
        values.put(QuesSQLiteHelper.QUESTION_TABLE_TEXT, getmQuestion());
        values.put(QuesSQLiteHelper.QUESTION_TABLE_GROUPID, getmGroupId());
        values.put(QuesSQLiteHelper.QUESTION_TABLE_TYPE, getmType());
        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mType);
        dest.writeInt(mGroupId);
        dest.writeInt(mAnswer ? 1: 0);
        dest.writeString(mQuestion);
        dest.writeString(mAnswerDetail);
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
