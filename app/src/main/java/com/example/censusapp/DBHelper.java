package com.example.censusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "users.db";
    public static final String TABLENAME = "userDetails";
    public static final int VER = 1;
    public DBHelper(Context context) {
        super(context, "UserDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLENAME +"(id integer primary key, userImg blob, name text, age text, gender text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists UserInfo";
        db.execSQL(query);
    }

    public Boolean saveData (String name, String age, String gender, String entryTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("age",age);
        contentValues.put("gender",gender);
        contentValues.put("entryTime",entryTime);

        long result = db.insert("UserInfo",null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from UserInfo", null);
        return cursor;
    }
}
