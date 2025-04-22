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
    private static final int DATABASE_VERSION = 2;

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
            e.printStackTrace();
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
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void addCart(String username, String product, float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product", product);
        cv.put("price", price);
        cv.put("otype", otype);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart", null, cv);
        db.close();
    }

    public int checkCart(String username, String product) {
        int result = 0;
        String[] str = {username, product};
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM cart WHERE username = ? AND product = ?", str);

        if (c.moveToFirst()) {
            result = 1;
        }
        c.close();
        db.close();
        return result;
    }
    public void removeCart(String username, String otype){
        String str[] = new String[2];
        str[0]= username;
        str[1]= otype;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart","username = ? and otype = ?",str);
        db.close();
    }

    public void addOrder(String username, String fullname, String address ,String contact ,int pincode,String date, String time , float price, String otype) {
        ContentValues cv = new ContentValues();
        cv.put("Username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("contactno", contact);
        cv.put("pincode", pincode);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("amount", price);
        cv.put("otype", otype);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("orderplace",null,cv);
        db.close();
    }

    public ArrayList<String> getCartData(String username, String otype) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase(); // Fixed missing `=` operator
        String[] str = new String[2]; // Use `String[]` instead of `String`
        str[0] = username;
        str[1] = otype;

        // Corrected query syntax and method call
        Cursor c = db.rawQuery("select * from cart where username = ? and otype = ?", str);
        if (c.moveToFirst()) {
            do {
                String product = c.getString(1);
                String price = c.getString(2);
                arr.add(product + "$" + price);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return arr;
    }
}