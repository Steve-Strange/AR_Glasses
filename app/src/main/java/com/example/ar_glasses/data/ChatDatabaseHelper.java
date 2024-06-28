package com.example.ar_glasses.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 2; // 增加版本号
    private static final String TAG = "ChatDatabaseHelper";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "conversation_id INTEGER, " +
                "content TEXT, " +
                "is_ai INTEGER, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")");
        db.execSQL("CREATE TABLE conversations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "topic TEXT, " +
                "last_message_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")");
        Log.d(TAG, "Tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE conversations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "topic TEXT, " +
                    "last_message_time DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            Log.d(TAG, "Conversations table added");
        }
    }
}
