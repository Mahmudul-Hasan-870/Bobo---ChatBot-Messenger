package com.example.bobo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ProfileDB"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version
    private static final String TABLE_NAME = "profiles"; // Table name
    private static final String COLUMN_ID = "id"; // Column name for profile ID
    private static final String COLUMN_NAME = "name"; // Column name for profile name
    private static final String COLUMN_EMAIL = "email"; // Column name for profile email
    private static final String COLUMN_PHONE = "phone"; // Column name for profile phone
    private static final String COLUMN_IMAGE = "image"; // Column name for profile image (stored as BLOB)

    public ProfileHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Call to superclass constructor
    }

    // Method invoked when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create the profiles table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTable); // Execute SQL statement to create the table
    }

    // Method invoked when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if it exists and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to retrieve all profiles from the database
    public Cursor getAllProfiles() {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // Execute raw SQL query to fetch all profiles
    }

    // Method to update a profile in the database based on ID
    public boolean updateProfile(int id, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        // Update the profile with the given ID using ContentValues
        long result = db.update(TABLE_NAME, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        return result != -1; // Return true if update was successful
    }

    // Method to insert a new profile into the database
    public boolean insertProfile(ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        // Insert new profile data using ContentValues
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insertion was successful
    }

    // Method to check if any profiles exist in the database
    public boolean doesProfileExist() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database instance
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME, null); // Execute raw SQL query to check existence
        boolean exists = cursor.getCount() > 0; // Check if cursor has any rows
        cursor.close(); // Close cursor to release resources
        return exists; // Return true if profiles exist, false otherwise
    }

    // Static method to return the table name
    public static String getTableName() {
        return TABLE_NAME; // Return the name of the table
    }
}
