package com.example.movierecommendation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBhandler extends SQLiteOpenHelper {
    public DBhandler(@Nullable Context context) {
        super(context, "users", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users (userName TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
    }

    public boolean checkUserName(String userName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userName = ?", new String[]{userName});

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    public boolean confirmUser(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userName = ? and password = ?", new String[]{userName, password});

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    public boolean insertData(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("password", password);

        long result = sqLiteDatabase.insert("users", null, contentValues);
        if(result == -1) {
            return false;
        }
        return true;
    }

//    public void updateStatus(String userName, String password, int status) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("userName", userName);
//        contentValues.put("password", password);
//        contentValues.put("loginStatus", status);
//
//        sqLiteDatabase.update("users", contentValues, "userName = ?", new String[]{userName});
//        sqLiteDatabase.close();
//    }

//    public int checkStatus(String userName) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where userName = ?", new String[]{userName,});
//
//        cursor.moveToFirst();
//        int status = cursor.getInt(2);
//
//        return status;
//    }
}
