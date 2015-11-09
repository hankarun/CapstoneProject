package com.hankarun.patienthistory.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.hankarun.patienthistory.helper.QuesSQLiteHelper;

public class Group implements Parcelable {
    int mId;
    String mGText;
    String mGDetail;

    public Group(Cursor cursor){
        setmId(cursor.getInt(cursor.getColumnIndex(QuesSQLiteHelper.GROUP_TABLE_ID)));
        setmGDetail(cursor.getString(cursor.getColumnIndex(QuesSQLiteHelper.GROUP_TABLE_DETAIL)));
        setmGText(cursor.getString(cursor.getColumnIndex(QuesSQLiteHelper.GROUP_TABLE_TEXT)));
    }

    public void setmId(int id) { mId = id;}
    public void setmGText(String gtext) { mGText = gtext;}
    public void setmGDetail(String gDetail) { mGDetail = gDetail;}

    public int getmId(){ return mId;}
    public String getmGText() { return mGText;}
    public String getmGDetail() { return mGDetail;}

    public String toString(){
        return mGText;
    }

    //Parcable implementation

    public Group(Parcel parcel){
        mId = parcel.readInt();
        String[] info = new String[2];
        parcel.readStringArray(info);
        mGText = info[0];
        mGDetail = info[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeStringArray(new String[] {mGText, mGDetail});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

}
