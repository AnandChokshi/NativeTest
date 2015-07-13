package com.inventure.test.nativetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.model.Validation;
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

    // Insert JSON into database
    public void insertJSON(List<Page> pages) {
        ContentValues contentValues = new ContentValues();

        for (Page page : pages) {
            contentValues.put(DbOpenHelper.PAGE_ORDER, page.getPage_order());
            contentValues.put(DbOpenHelper.QUESTIONS_ID, page.getCondition().getQid());
            contentValues.put(DbOpenHelper.ANSWER, page.getCondition().getAnswer());
            contentValues.put(DbOpenHelper.STATUS, 0);

            long page_id = sqLiteDatabase.insert(DbOpenHelper.PAGE_TABLE_NAME, null, contentValues);
            // Insert Questions into Database by page
            insertQuestions(page_id, page.getQuestions());
            contentValues.clear();
        }
    }

    // Insert all questions in Database
    private void insertQuestions(long page_id, List<Question> questionList) {
        ContentValues contentValues = new ContentValues();
        String type;
        Long que_id;

        for (Question question : questionList) {
            contentValues.put(DbOpenHelper.PAGE_ID, page_id);
            contentValues.put(DbOpenHelper.QUESTIONS_SERVER_ID, question.getQuestion_server_id());

            type = question.getType();
            contentValues.put(DbOpenHelper.TYPE, type);
            contentValues.put(DbOpenHelper.LABEL, question.getLabel());

            if (type.equals(QuestionType.TEXT_BOX) || type.equals(QuestionType.TEXT_AREA))
                contentValues.put(DbOpenHelper.PLACEHOLDER, question.getPlaceHolder());

            contentValues.put(DbOpenHelper.ANSWER, "Empty");

            que_id = sqLiteDatabase.insert(DbOpenHelper.QUESTIONS_TABLE_NAME, null, contentValues);

            // IF Default values exists insert them in the default_values table
            if (question.getDefaultValues().size() > 0)
                insertDefaultValues(que_id, question.getDefaultValues());

            // Insert Validation data
            insertValidation(que_id, question.getValidation());

            // IF questions have options then insert them in options table
            if (type.equals(QuestionType.RADIO) || type.equals(QuestionType.SPINNER) || type.equals(QuestionType.CHECKBOX))
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

    // Read Page by checking condition and status
    public Page readPage() {
        Page page = new Page();
        boolean loop = true;

        while (loop) {
            String query = "SELECT * FROM " + DbOpenHelper.PAGE_TABLE_NAME + " WHERE " +
                    DbOpenHelper.STATUS + " = 0 LIMIT 1";

            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            int page_id;

            if (cursor.moveToNext()) {
                page_id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.PAGE_ID));
                int question_id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.QUESTIONS_ID));

                // if question_id is zero then load the page otherwise check condition
                if (question_id != 0) {
                    // Check that page satisfies the condition
                    query = "SELECT * FROM " + DbOpenHelper.QUESTIONS_TABLE_NAME + " WHERE " +
                            DbOpenHelper.QUESTIONS_ID + " = " + question_id + " AND " +
                            DbOpenHelper.ANSWER + " = '" + cursor.getString(cursor.getColumnIndex(DbOpenHelper.ANSWER)) + "'";

                    cursor = sqLiteDatabase.rawQuery(query, null);

                    // if condition satisfies then load the page otherwise change the status and call method again
                    if (!cursor.moveToNext()) {
                        ContentValues values = new ContentValues();
                        values.put(DbOpenHelper.STATUS, 1);
                        sqLiteDatabase.update(DbOpenHelper.PAGE_TABLE_NAME, values, DbOpenHelper.PAGE_ID + " = ?",
                                new String[]{String.valueOf(page_id)});

                        cursor.close();
                        continue;
                    } else {
                        loadPage(page_id, page);
                    }
                } else {
                    loadPage(page_id, page);
                }
                page.setPage_id(page_id);
                loop = false;
            }
            cursor.close();
        }
        return page;
    }

    // Load page from database
    public void loadPage(long page_id, Page page) {
        String query = "SELECT * FROM " + DbOpenHelper.QUESTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.PAGE_ID + " = " + page_id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        ArrayList<Question> questions = new ArrayList<>();

        while (cursor.moveToNext()) {
            loadQuestions(cursor, questions);
        }
        page.setQuestions(questions);
        cursor.close();
    }

    private void loadQuestions(Cursor cursor, ArrayList<Question> questions) {
        Question question = new Question();
        question.setQuestion_id(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.QUESTIONS_ID)));
        question.setQuestion_server_id(cursor.getInt(cursor.getColumnIndex(DbOpenHelper.QUESTIONS_SERVER_ID)));
        question.setType(cursor.getString(cursor.getColumnIndex(DbOpenHelper.TYPE)));
        question.setLabel(cursor.getString(cursor.getColumnIndex(DbOpenHelper.LABEL)));
        question.setAnswer(cursor.getString(cursor.getColumnIndex(DbOpenHelper.ANSWER)));
        question.setPlaceHolder(cursor.getString(cursor.getColumnIndex(DbOpenHelper.PLACEHOLDER)));

        // Get default values
        String query = "SELECT " + DbOpenHelper.VALUE + " FROM " +
                DbOpenHelper.DEFAULT_VALUES_TABLE_NAME + " WHERE " +
                DbOpenHelper.QUESTIONS_ID + " = " + question.getQuestion_id();
        Cursor cursorInside = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> defaultValues = new ArrayList<>();

        while (cursorInside.moveToNext()) {
            defaultValues.add(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.VALUE)));
        }
        question.setDefaultValues(defaultValues);

        // Get Validation
        query = "SELECT * FROM " + DbOpenHelper.VALIDATION_TABLE_NAME + " WHERE " +
                DbOpenHelper.QUESTIONS_ID + " = " + question.getQuestion_id();
        cursorInside = sqLiteDatabase.rawQuery(query, null);
        Validation validation = new Validation();

        cursorInside.moveToNext();

        validation.setRequired(cursorInside.getInt(cursorInside.getColumnIndex(DbOpenHelper.REQUIRED)));
        validation.setValidation_type(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.VALIDATION_TYPE)));
        validation.setRegex(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.REGEX)));
        validation.setError_message(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.ERROR_MESSAGE)));
        question.setValidation(validation);

        // Get Options
        query = "SELECT " + DbOpenHelper.OPTION + " FROM " +
                DbOpenHelper.OPTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.QUESTIONS_ID + " = " + question.getQuestion_id();
        cursorInside = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> options = new ArrayList<>();

        while (cursorInside.moveToNext()) {
            options.add(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.OPTION)));
        }
        question.setOptions(options);
        cursorInside.close();

        questions.add(question);
    }

    public void insertAnswer(Page page) {
        ContentValues values;
        for (Question question : page.getQuestions()) {
            values = new ContentValues();
            values.put(DbOpenHelper.ANSWER, question.getAnswer());

            sqLiteDatabase.update(DbOpenHelper.QUESTIONS_TABLE_NAME, values, DbOpenHelper.QUESTIONS_ID + " = ?",
                    new String[]{String.valueOf(question.getQuestion_id())});
        }
        values = new ContentValues();
        values.put(DbOpenHelper.STATUS, 1);
        sqLiteDatabase.update(DbOpenHelper.PAGE_TABLE_NAME, values, DbOpenHelper.PAGE_ID + " = ?",
                new String[]{String.valueOf(page.getPage_id())});
    }
}