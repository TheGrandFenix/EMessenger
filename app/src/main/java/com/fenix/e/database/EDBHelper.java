package com.fenix.e.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EDBHelper extends SQLiteOpenHelper {
    //Database Properties
    private static final String DATABASE_NAME = "E.db";
    private static final int DATABASE_VERSION = 1;

    //Passing database properties to parent constructor
    public EDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Database creation procedure
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CHAT_TABLE ("
                + "SESSION_ID INTEGER NOT NULL, "
                + "LAST_UPDATE_DT INTEGER NOT NULL, "
                + "PARTICIPANTS BLOB NOT NULL, "
                + "PRIMARY KEY (SESSION_ID))");

        db.execSQL("CREATE TABLE MESSAGE_TABLE ("
                + "MESSAGE_ID INTEGER NOT NULL, "
                + "SESSION_ID INTEGER NOT NULL, "
                + "SENDER_ID INTEGER NOT NULL, "
                + "TIMESTAMP INTEGER NOT NULL, "
                + "CONTENT BLOB NOT NULL, "
                + "PRIMARY KEY (MESSAGE_ID), "
                + "FOREIGN KEY (SESSION_ID) REFERENCES CHAT_TABLE(SESSION_ID) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE USER_TABLE ("
                + "USER_ID INTEGER NOT NULL, "
                + "USERNAME TEXT NOT NULL, "
                + "NAME TEXT NOT NULL, "
                + "UNIQUE (USERNAME),"
                + "PRIMARY KEY (USER_ID))");
    }

    //Database upgrade procedure
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CHAT_TABLE");
        db.execSQL("DROP TABLE IF EXISTS MESSAGE_TABLE");
        db.execSQL("DROP TABLE IF EXISTS USER_TABLE");
        onCreate(db);
    }
}
