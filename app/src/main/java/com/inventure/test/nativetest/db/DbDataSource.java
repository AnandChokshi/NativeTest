package com.inventure.test.nativetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.QuestionMod;
import com.inventure.test.nativetest.model.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anand on 6/16/2015.
 */
public class DbDataSource {
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;

    public DbDataSource(Context context) {
        sqLiteOpenHelper = new DbOpenHelper(context);
    }

    // Open DB
    public void open() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    // Close DB
    public void close() {
        sqLiteDatabase.close();
    }

    // Insert JSON into database
    public void insertJSON(List<Page> pages) {
        ContentValues contentValues = new ContentValues();

        for (Page page : pages) {
            contentValues.put(DbOpenHelper.PAGE_ORDER, page.getPage_order());
            contentValues.put(DbOpenHelper.QUESTIONS_ID, page.getCondition().getQid());
            contentValues.put(DbOpenHelper.ANSWER, page.getCondition().getAnswer());
            contentValues.put(DbOpenHelper.STATUS, 0);

            sqLiteDatabase.insert(DbOpenHelper.PAGE_TABLE_NAME, null, contentValues);
            // Insert Questions into Database by page
            insertQuestions(page.getQuestions());
            contentValues.clear();
        }
    }

    // Insert all questions in Database
    private void insertQuestions(List<QuestionMod> questionList) {
        ContentValues contentValues = new ContentValues();
        String type;
        Long que_id;

        for (QuestionMod question : questionList) {
            contentValues.put(DbOpenHelper.QUESTIONS_SERVER_ID, question.getQuestion_server_id());

            type = question.getType();
            contentValues.put(DbOpenHelper.TYPE, type);
            contentValues.put(DbOpenHelper.LABEL, question.getLabel());

            if (type.equals("textbox") || type.equals("textarea"))
                contentValues.put(DbOpenHelper.PLACEHOLDER, question.getPlaceHolder());

            contentValues.put(DbOpenHelper.ANSWER, "Empty");

            que_id = sqLiteDatabase.insert(DbOpenHelper.QUESTIONS_TABLE_NAME, null, contentValues);

            // IF Default values exists insert them in the default_values table
            if (question.getDefaultValues().size() > 0)
                insertDefaultValues(que_id, question.getDefaultValues());

            // Insert Validation data
            insertValidation(que_id, question.getValidation());

            // IF questions have options then insert them in options table
            if (type.equals("radio") || type.equals("spinner") || type.equals("checkbox"))
                insertOptions(que_id, question.getOptions());

            // clear current question values
            contentValues.clear();
        }
    }

    // Insert Default values if available
    private void insertDefaultValues(long que_id, ArrayList<String> default_values) {
        ContentValues contentValues = new ContentValues();
        for (String values : default_values) {
            contentValues.put(DbOpenHelper.QUESTIONS_ID, que_id);
            contentValues.put(DbOpenHelper.VALUE, values);
            sqLiteDatabase.insert(DbOpenHelper.DEFAULT_VALUES_TABLE_NAME, null, contentValues);

            contentValues.clear();
        }
    }

    // Insert Validation Data
    private void insertValidation(long que_id, Validation validation) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbOpenHelper.QUESTIONS_ID, que_id);
        contentValues.put(DbOpenHelper.REQUIRED, validation.getRequired());
        contentValues.put(DbOpenHelper.VALIDATION_TYPE, validation.getValidation_type());
        contentValues.put(DbOpenHelper.REGEX, validation.getRegex());
        contentValues.put(DbOpenHelper.ERROR_MESSAGE, validation.getError_message());

        sqLiteDatabase.insert(DbOpenHelper.VALIDATION_TABLE_NAME, null, contentValues);
    }

    // Insert options if available
    private void insertOptions(Long que_id, ArrayList<String> options) {
        ContentValues contentValues = new ContentValues();

        for (String option : options) {
            contentValues.put(DbOpenHelper.QUESTIONS_ID, que_id);
            contentValues.put(DbOpenHelper.OPTION, option);

            sqLiteDatabase.insert(DbOpenHelper.OPTIONS_TABLE_NAME, null, contentValues);

            contentValues.clear();
        }
    }

    // Read Page
    public Page readPage() {
        String query = "SELECT * FROM " + DbOpenHelper.PAGE_TABLE_NAME + " WHERE " +
                DbOpenHelper.STATUS + " = 0 LIMIT 1";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
    }
/*
    // Read from Questions
    public Question readQuestion() {
        String query = "SELECT * FROM " + DbOpenHelper.QUESTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.ANSWER + " = 'Empty' LIMIT 1";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Question question = new Question();
        while (cursor.moveToNext()) {
            question.setId(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.ID)));
            question.setQuestion(cursor.getString(cursor.getColumnIndex(DbOpenHelper.QUESTION)));
            question.setType(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.TYPE)));
            if (question.getType() == QuestionType.RADIO || question.getType() == QuestionType.CHECKBOX)
                readOptions(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.QUE_ID)), question);
            question.setAnswer(cursor.getString(cursor.getColumnIndex(DbOpenHelper.ANSWER)));
        }
        cursor.close();

        return question;
    }

    // read from options
    private void readOptions(int que_id, Question question) {
        String query = "SELECT " + DbOpenHelper.CONTENT + " FROM " +
                DbOpenHelper.OPTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.QUE_ID + " = " + que_id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> options = new ArrayList<>();

        while (cursor.moveToNext()) {
            options.add(cursor.getString(cursor.getColumnIndex(DbOpenHelper.CONTENT)));
        }

        cursor.close();
        question.setOptions(options);
    }

    public void insertAnswer(int qId, String answer) {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.ANSWER, answer);
        sqLiteDatabase.update(DbOpenHelper.QUESTIONS_TABLE_NAME, values, DbOpenHelper.ID + " = ?",
                new String[]{String.valueOf(qId)});
    } */
}