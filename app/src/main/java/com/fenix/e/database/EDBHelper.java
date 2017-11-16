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
        db.execSQL("CREATE CHAT_TABLE ("
                + "SESSION_ID INTEGER UNSIGNED NOT NULL, "
                + "LAST_UPDATE_DT INTEGER UNSIGNED NOT NULL, "
                + "PARTICIPANTS BLOB NOT NULL, "
                + "PRIMARY KEY (SESSION_ID))");

        db.execSQL("CREATE MESSAGE_TABLE ("
                + "MESSAGE_ID INTEGER UNSIGNED NOT NULL, "
                + "SESSION_ID INTEGER UNSIGNED NOT NULL, "
                + "SENDER_ID INTEGER UNSIGNED NOT NULL, "
                + "TIMESTAMP INTEGER UNSIGNED NOT NULL, "
                + "CONTENT BLOB NOT NULL, "
                + "PRIMARY KEY (MESSAGE_ID), "
                + "FOREIGN KEY (SESSION_ID) REFERENCES CHAT_TABLE(SESSION_ID) ON DELETE CASCADE)");

        db.execSQL("CREATE USER_TABLE ("
                + "USER_ID INTEGER UNSIGNED NOT NULL, "
                + "USERNAME TEXT NOT NULL, "
                + "NAME TEXT NOT NULL, "
                + "UNIQUE (USERNAME) "
                + "PRIMARY KEY (USER_ID))");
    }

    //Database upgrade procedure
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CHAT_TABLE, MESSAGE_TABLE, USER_TABLE");
        onCreate(db);
    }
}
