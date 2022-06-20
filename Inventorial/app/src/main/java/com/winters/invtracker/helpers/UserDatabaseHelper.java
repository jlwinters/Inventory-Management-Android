package com.winters.invtracker.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.winters.invtracker.classes.User;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    // Database information
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "UserManager1.db";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_USERNAME = "user_username";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Data table SQL query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + " TEXT,"
                + COLUMN_USER_USERNAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop data table SQL query
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    /**
     * Create a new user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USERNAME, user.getUser());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * Check if the username exists
     */
    public boolean checkUser(String user) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = {user};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
    /**
     * Check if username and password exists
     */
    public boolean checkUser(String user, String password) {
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_USERNAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {user, password};
        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }
}