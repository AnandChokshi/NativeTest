package com.inventure.test.nativetest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anand on 6/16/2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    // db name
    private static final String DB_NAME = "questions.db";

    // Version
    private static final int DB_VERSION = 1;

    // Name of the table Questions Column
    public static final String QUESTIONS_TABLE_NAME = "questions";
    public static final String QUE_ID = "que_id";
    public static final String Q_ID = "qid";
    public static final String QUESTION = "question";
    public static final String TYPE = "type";
    public static final String ANSWER = "answer";

    // Name of the table Options Column
    public static final String OPTIONS_TABLE_NAME = "options";
    public static final String O_ID = "oid";
    public static final String CONTENT = "content";


    // Query to Create both tables
    private static final String QUESTIONS_TABLE_CREATE =
            "CREATE TABLE " + QUESTIONS_TABLE_NAME + " (" +
                    QUE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Q_ID + " INTEGER, " +
                    QUESTION + " TEXT, " +
                    TYPE + " NUMERIC, " +
                    ANSWER + " TEXT " +
                    ")";
    private static final String OPTIONS_TABLE_CREATE =
            "CREATE TABLE " + OPTIONS_TABLE_NAME + " (" +
                    O_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUE_ID + " INTEGER, " +
                    CONTENT + " TEXT, " +
                    "FOREIGN KEY(" + QUE_ID + ") REFERENCES " + QUESTIONS_TABLE_NAME + "(" + QUE_ID + "))";


    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUESTIONS_TABLE_CREATE);
        db.execSQL(OPTIONS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPTIONS_TABLE_NAME);
        onCreate(db);
    }
}
