package com.inventure.test.nativetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.util.QuestionType;

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

    // Insert all questions in Database
    public void insertQuestions(List<Question> questionList) {
        ContentValues contentValues = new ContentValues();
        int type;
        Long que_id;

        for (Question question : questionList) {
            contentValues.put(DbOpenHelper.Q_ID, question.getId());
            contentValues.put(DbOpenHelper.QUESTION, question.getQuestion());

            type = question.getType();
            contentValues.put(DbOpenHelper.TYPE, type);
            contentValues.put(DbOpenHelper.ANSWER, "Empty");

            que_id = sqLiteDatabase.insert(DbOpenHelper.QUESTIONS_TABLE_NAME, null, contentValues);
            // IF questions have options then insert them in options table
            if (type == QuestionType.RADIO || type == QuestionType.CHECKBOX)
                insertOptions(que_id, question);

            // clear current question values
            contentValues.clear();
        }
    }

    // Insert options if available
    private void insertOptions(Long que_id, Question question) {
        ContentValues contentValues = new ContentValues();

        for (String content : question.getOptions()) {
            contentValues.put(DbOpenHelper.QUE_ID, que_id);
            contentValues.put(DbOpenHelper.CONTENT, content);

            sqLiteDatabase.insert(DbOpenHelper.OPTIONS_TABLE_NAME, null, contentValues);

            contentValues.clear();
        }
    }

    // Read from Questions
    public Question readQuestion() {
        String query = "SELECT * FROM " + DbOpenHelper.QUESTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.ANSWER + " = 'Empty' LIMIT 1";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Question question = new Question();
        while (cursor.moveToNext()) {
            question.setId(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.Q_ID)));
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
        sqLiteDatabase.update(DbOpenHelper.QUESTIONS_TABLE_NAME, values, DbOpenHelper.Q_ID + " = ?",
                new String[]{String.valueOf(qId)});
    }
}