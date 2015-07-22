package com.inventure.test.nativetest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anand on 6/16/2015.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    // db name
    private static final String DB_NAME = "native.db";

    // Version
    private static final int DB_VERSION = 1;

    // Name of the tables
    public static final String SECTION_TABLE_NAME = "section";
    public static final String PAGE_TABLE_NAME = "page";
    public static final String QUESTIONS_TABLE_NAME = "questions";
    public static final String OPTIONS_TABLE_NAME = "options";
    public static final String DEFAULT_VALUES_TABLE_NAME = "default_values";
    public static final String VALIDATION_TABLE_NAME = "validation";
    public static final String PAGE_CONDITION_TABLE_NAME = "page_condition";
    public static final String SECTION_CONDITION_TABLE_NAME = "section_condition";

    // Section table Columns
    public static final String SECTION_ID = "section_id";
    public static final String CONFIRMATION = "confirmation";
    public static final String CONDITION = "condition";
    public static final String STATUS = "status";

    // Page table Columns
    public static final String PAGE_ID = "page_id";
    public static final String CREATED_TIME_STAMP = "created_time_stamp";
    public static final String MODIFIED_TIME_STAMP = "modified_time_stamp";

    // Questions table Columns
    public static final String QUESTIONS_ID = "questions_id";
    public static final String QUESTIONS_SERVER_ID = "questions_server_id";
    public static final String TYPE = "type";
    public static final String LABEL = "label";
    public static final String PLACEHOLDER = "placeholder";
    public static final String ANSWER = "answer";

    // Options table Columns
    public static final String OPTIONS_ID = "options_id";
    public static final String OPTION = "option";

    // Default Values table Columns
    public static final String DEFAULT_VALUES_ID = "default_values_id";
    public static final String VALUE = "value";

    // Validation table Columns
    public static final String VALIDATION_ID = "validation_id";
    public static final String REQUIRED = "requried";
    public static final String VALIDATION_TYPE = "validation_type";
    public static final String REGEX = "regex";
    public static final String ERROR_MESSAGE = "error_message";

    // Condition tables Columns
    public static final String CONDITION_ID = "condition_id";

    // Query to Create both tables
    private static final String SECTION_TABLE_CREATE =
            "CREATE TABLE " + SECTION_TABLE_NAME + " (" +
                    SECTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONFIRMATION + " INTEGER, " +
                    CONDITION + " INTEGER, " +
                    STATUS + " INTEGER" +
                    ")";
    private static final String PAGE_TABLE_CREATE =
            "CREATE TABLE " + PAGE_TABLE_NAME + " (" +
                    PAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SECTION_ID + " INTEGER, " +
                    CONDITION + " INTEGER, " +
                    STATUS + " INTEGER, " +
                    CREATED_TIME_STAMP + " TEXT, " +
                    MODIFIED_TIME_STAMP + " TEXT, " +
                    "FOREIGN KEY(" + SECTION_ID + ") REFERENCES " + SECTION_TABLE_NAME + "(" + SECTION_ID + "))";
    private static final String QUESTIONS_TABLE_CREATE =
            "CREATE TABLE " + QUESTIONS_TABLE_NAME + " (" +
                    QUESTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PAGE_ID + " INTEGER, " +
                    QUESTIONS_SERVER_ID + " INTEGER, " +
                    TYPE + " NUMERIC, " +
                    LABEL + " TEXT, " +
                    PLACEHOLDER + " TEXT, " +
                    ANSWER + " TEXT, " +
                    "FOREIGN KEY(" + PAGE_ID + ") REFERENCES " + PAGE_TABLE_NAME + "(" + PAGE_ID + "))";
    private static final String OPTIONS_TABLE_CREATE =
            "CREATE TABLE " + OPTIONS_TABLE_NAME + " (" +
                    OPTIONS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTIONS_ID + " INTEGER, " +
                    OPTION + " TEXT, " +
                    "FOREIGN KEY(" + QUESTIONS_ID + ") REFERENCES " + QUESTIONS_TABLE_NAME + "(" + QUESTIONS_ID + "))";
    private static final String DEFAULT_VALUES_TABLE_CREATE =
            "CREATE TABLE " + DEFAULT_VALUES_TABLE_NAME + " (" +
                    DEFAULT_VALUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTIONS_ID + " INTEGER, " +
                    VALUE + " TEXT, " +
                    "FOREIGN KEY(" + QUESTIONS_ID + ") REFERENCES " + QUESTIONS_TABLE_NAME + "(" + QUESTIONS_ID + "))";
    private static final String VALIDATION_TABLE_CREATE =
            "CREATE TABLE " + VALIDATION_TABLE_NAME + " (" +
                    VALIDATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUESTIONS_ID + " INTEGER, " +
                    REQUIRED + " INTEGER, " +
                    VALIDATION_TYPE + " TEXT, " +
                    REGEX + " TEXT, " +
                    ERROR_MESSAGE + " TEXT, " +
                    "FOREIGN KEY(" + QUESTIONS_ID + ") REFERENCES " + QUESTIONS_TABLE_NAME + "(" + QUESTIONS_ID + "))";
    private static final String PAGE_CONDITION_TABLE_CREATE =
            "CREATE TABLE " + PAGE_CONDITION_TABLE_NAME + " (" +
                    CONDITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PAGE_ID + " INTEGER, " +
                    QUESTIONS_SERVER_ID + " TEXT, " +
                    ANSWER + " TEXT, " +
                    "FOREIGN KEY(" + PAGE_ID + ") REFERENCES " + PAGE_TABLE_NAME + "(" + PAGE_ID + "))";
    private static final String SECTION_CONDITION_TABLE_CREATE =
            "CREATE TABLE " + SECTION_CONDITION_TABLE_NAME + " (" +
                    CONDITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SECTION_ID + " INTEGER, " +
                    QUESTIONS_SERVER_ID + " TEXT, " +
                    ANSWER + " TEXT, " +
                    "FOREIGN KEY(" + SECTION_ID + ") REFERENCES " + SECTION_TABLE_NAME + "(" + SECTION_ID + "))";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SECTION_TABLE_CREATE);
        db.execSQL(PAGE_TABLE_CREATE);
        db.execSQL(QUESTIONS_TABLE_CREATE);
        db.execSQL(OPTIONS_TABLE_CREATE);
        db.execSQL(DEFAULT_VALUES_TABLE_CREATE);
        db.execSQL(VALIDATION_TABLE_CREATE);
        db.execSQL(PAGE_CONDITION_TABLE_CREATE);
        db.execSQL(SECTION_CONDITION_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SECTION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OPTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEFAULT_VALUES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VALIDATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QUESTIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PAGE_CONDITION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SECTION_CONDITION_TABLE_NAME);
        onCreate(db);
    }
}