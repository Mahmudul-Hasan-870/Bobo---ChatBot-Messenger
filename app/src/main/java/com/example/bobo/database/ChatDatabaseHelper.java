package com.example.bobo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat_db"; // Database name
    private static final int DATABASE_VERSION = 1; // Database version

    // Table and column names for chat titles
    public static final String TABLE_CHAT_TITLE = "chat_title"; // Table name for chat titles
    public static final String COLUMN_CHAT_ID = "id"; // Column name for chat ID
    public static final String COLUMN_TITLE = "title"; // Column name for chat title

    // Table and column names for chat messages
    public static final String TABLE_MESSAGES = "messages"; // Table name for chat messages
    public static final String COLUMN_MESSAGE_ID = "message_id"; // Column name for message ID
    public static final String COLUMN_MESSAGE = "message"; // Column name for message content
    public static final String COLUMN_IS_BOT = "is_bot"; // Column name for identifying if message is from bot
    public static final String COLUMN_TIMESTAMP = "timestamp"; // Column name for message timestamp
    public static final String COLUMN_CHAT_ID_FK = "chat_id"; // Foreign key column linking messages to chat titles

    // SQL statement to create table for chat titles
    private static final String CREATE_TABLE_CHAT_TITLE = "CREATE TABLE "
            + TABLE_CHAT_TITLE + "("
            + COLUMN_CHAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL"
            + ")";

    // SQL statement to create table for chat messages
    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "
            + TABLE_MESSAGES + "("
            + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MESSAGE + " TEXT NOT NULL,"
            + COLUMN_IS_BOT + " INTEGER NOT NULL,"
            + COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_CHAT_ID_FK + " INTEGER,"
            + " FOREIGN KEY (" + COLUMN_CHAT_ID_FK + ") REFERENCES " + TABLE_CHAT_TITLE + "(" + COLUMN_CHAT_ID + ")"
            + ")";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Call to superclass constructor
    }

    // Method invoked when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHAT_TITLE); // Execute SQL to create chat title table
        db.execSQL(CREATE_TABLE_MESSAGES); // Execute SQL to create messages table
    }

    // Method invoked when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES); // Drop messages table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_TITLE); // Drop chat title table if exists
        onCreate(db); // Recreate tables
    }

    // Method to save a new chat title into the database
    public long saveChatTitle(String title) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        ContentValues values = new ContentValues(); // Create ContentValues to store data
        values.put(COLUMN_TITLE, title); // Put title value into ContentValues
        return db.insert(TABLE_CHAT_TITLE, null, values); // Insert data into chat title table
    }

    // Method to save a new message into the database
    public void saveMessage(long chatId, String message, boolean isBot) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        ContentValues values = new ContentValues(); // Create ContentValues to store data
        values.put(COLUMN_CHAT_ID_FK, chatId); // Put chat ID value into ContentValues
        values.put(COLUMN_MESSAGE, message); // Put message content into ContentValues
        values.put(COLUMN_IS_BOT, isBot ? 1 : 0); // Put isBot flag (1 for true, 0 for false) into ContentValues
        db.insert(TABLE_MESSAGES, null, values); // Insert data into messages table
    }

    // Method to retrieve all chat titles from the database
    public Cursor getAllChatTitles() {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database instance
        return db.query(TABLE_CHAT_TITLE, null, null, null, null, null, null); // Query all rows from chat title table
    }

    // Method to retrieve messages by chat ID from the database
    public Cursor getMessagesByChatId(long chatId) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database instance
        return db.query(TABLE_MESSAGES, null, COLUMN_CHAT_ID_FK + "=?", new String[]{String.valueOf(chatId)}, null, null, null); // Query messages for specific chat ID
    }
}
