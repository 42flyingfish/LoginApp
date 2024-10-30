package com.example.loginapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

// Some of the code here is written following the examples given on
// https://developer.android.com/training/data-storage/sqlite

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "userdata";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String ID_COL ="userID";
    private static final String PASSWORD_COL ="Password";
    private static final String USERNAME_COL ="userName";


    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Database", "Database on create ran");
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PASSWORD_COL + " TEXT,"
                + USERNAME_COL + " TEXT)";
        sqLiteDatabase.execSQL(query);
        Log.d("Database", query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues row = new ContentValues();
        row.put(USERNAME_COL, user.getUsername());
        row.put(PASSWORD_COL, user.getPassword());
        long result = db.insert(TABLE_NAME, null, row);
        return result != -1;
    }

    // Check if user credentials match. Returns True if user does.
    // TODO check for sql injection problems
    public boolean userMatch(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT 1 FROM " + TABLE_NAME
                + " WHERE " + USERNAME_COL + "=? AND " + PASSWORD_COL + "=?;";

        String[] args = new String[]{user.getUsername(), user.getPassword()};

        Cursor thing = db.rawQuery(query, args);

        boolean result = thing.getCount() > 0;

        thing.close();

        return result;
    }

    // Check if user credentials match. Returns True if user does.
    // TODO check for sql injection problems
    public boolean userExists(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT 1 FROM " + TABLE_NAME
                + " WHERE " + USERNAME_COL + "=?;";

        String[] args = new String[]{user.getUsername()};

        Cursor thing = db.rawQuery(query, args);

        boolean result = thing.getCount() > 0;

        thing.close();

        return result;
    }
}
