package com.example.medisync;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mediSync.db";
    private static final int DATABASE_VERSION = 2; // Incremented version number

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "CREATE TABLE users (username TEXT PRIMARY KEY, email TEXT, password TEXT)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart(username text, product text, price float, otype text)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace(username text, fullname text, address text, contactno text, pincode int, date text, time text, amount float, otype text)";
        sqLiteDatabase.execSQL(qry3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Example: Add a new column to the users table
            String alterUsersTable = "ALTER TABLE users ADD COLUMN phone TEXT"; // Adding a phone number column
            sqLiteDatabase.execSQL(alterUsersTable);
        }
    }

    public void register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);

        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.insert("users", null, cv);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public int login(String username, String password) {
        int result = 0;
        String[] str = new String[]{username, password};
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getReadableDatabase();
            c = db.rawQuery("SELECT * FROM users WHERE username=? AND password=?", str);
            if (c.moveToFirst()) {
                result = 1; // User found
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return result;
    }
}