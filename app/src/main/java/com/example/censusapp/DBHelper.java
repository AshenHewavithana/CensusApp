package com.example.censusapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "user.db";
    public static final String TABLENAME = "userData";
    public static final int VER = 1;
    public DBHelper(Context context) {
        super(context, DBNAME, null, VER);
    }

    // creates a table in the database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLENAME +"(id integer primary key, img blob, name text, age text, gender text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists UserInfo";
        db.execSQL(query);
    }

    // saves the user data to the table
    public boolean saveUserData(int id, String name, String age, String gender, byte[] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("age", age);
        cv.put("gender", gender);
        cv.put("img", img);
        long result = db.insert(TABLENAME,null,cv);
        if(result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    // retrieves all data saved in the database
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLENAME,null);
        return res;
    }

    // clears the data of the table
    public void clear(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLENAME,null,null);
        db.close();
    }
}
