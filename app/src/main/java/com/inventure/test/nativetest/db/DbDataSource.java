package com.inventure.test.nativetest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.inventure.test.nativetest.model.Condition;
import com.inventure.test.nativetest.model.Option;
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

    // Insert Json into database // Insert pages
    public void insertJSON(ArrayList<Page> pages) {
        ContentValues contentValues = new ContentValues();

        for (Page page : pages) {
            if (page.getConditions().size() > 0) {
                contentValues.put(DbOpenHelper.CONDITION, 1);
            } else {
                contentValues.put(DbOpenHelper.CONDITION, 0);
            }
            contentValues.put(DbOpenHelper.STATUS, 0);
            contentValues.put(DbOpenHelper.CREATED_TIME_STAMP, "Empty");
            contentValues.put(DbOpenHelper.MODIFIED_TIME_STAMP, "Empty");

            long page_id = sqLiteDatabase.insert(DbOpenHelper.PAGE_TABLE_NAME, null, contentValues);
            // Insert Condition if exists
            if (contentValues.getAsInteger(DbOpenHelper.CONDITION) == 1) {
                insertCondition(page_id, page.getConditions());
            }

            // Insert Questions into Database by page
            insertQuestions(page_id, page.getQuestions());
            contentValues.clear();
        }
    }


    // Insert condition into database
    private void insertCondition(long id, List<Condition> conditions) {
        ContentValues contentValues = new ContentValues();

        for (Condition condition : conditions) {
            contentValues.put(DbOpenHelper.PAGE_ID, id);
            contentValues.put(DbOpenHelper.QUESTIONS_SERVER_ID, condition.getQuestion_server_id());
            contentValues.put(DbOpenHelper.ANSWER, condition.getAnswer());

            sqLiteDatabase.insert(DbOpenHelper.PAGE_CONDITION_TABLE_NAME, null, contentValues);

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
        contentValues.put(DbOpenHelper.SERVER_VALIDATION, validation.getServer_validation());
        contentValues.put(DbOpenHelper.VALIDATION_TYPE, validation.getValidation_type());
        contentValues.put(DbOpenHelper.REGEX, validation.getRegex());
        contentValues.put(DbOpenHelper.ERROR_MESSAGE, validation.getError_message());

        sqLiteDatabase.insert(DbOpenHelper.VALIDATION_TABLE_NAME, null, contentValues);
    }

    // Insert options if available
    private void insertOptions(Long que_id, ArrayList<Option> options) {
        ContentValues contentValues = new ContentValues();

        for (Option option : options) {
            contentValues.put(DbOpenHelper.QUESTIONS_ID, que_id);
            contentValues.put(DbOpenHelper.LABEL, option.getLabel());
            contentValues.put(DbOpenHelper.VALUE, option.getValue());

            sqLiteDatabase.insert(DbOpenHelper.OPTIONS_TABLE_NAME, null, contentValues);

            contentValues.clear();
        }
    }

    // Read Page by checking condition and status (Reads only one page)
    public Page readPage() {
        Page page = new Page();
        String query;
        Cursor cursor;
        int page_id;

        while (true) {
            query = "SELECT * FROM " + DbOpenHelper.PAGE_TABLE_NAME + " WHERE " +
                    DbOpenHelper.STATUS + " = 0 LIMIT 1";

            cursor = sqLiteDatabase.rawQuery(query, null);

            if (cursor.moveToNext()) {
                page_id = cursor.getInt(cursor.getColumnIndex(DbOpenHelper.PAGE_ID));
                page.setPage_id(page_id);
                if (cursor.getInt(cursor.getColumnIndex(DbOpenHelper.CONDITION)) == 1) {
                    if (!checkCondition(page_id, DbOpenHelper.PAGE_TABLE_NAME,
                            DbOpenHelper.PAGE_CONDITION_TABLE_NAME, DbOpenHelper.PAGE_ID)) {
                        continue;
                    }
                }
                loadPage(page_id, page);
            }
            cursor.close();
            break;
        }

        return page;
    }

    // Check Condition for page
    private boolean checkCondition(int id, String tableName, String conditionTableName, String columnName) {
        String query = "SELECT * FROM " + conditionTableName + " WHERE " +
                columnName + " = " + id;

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        // variable to confirm that every condition of is satisfied
        int count = cursor.getCount();

        while (cursor.moveToNext()) {
            count--;
            query = "SELECT * FROM " + DbOpenHelper.QUESTIONS_TABLE_NAME + " WHERE " +
                    DbOpenHelper.QUESTIONS_SERVER_ID + " = '" + cursor.getString(cursor.getColumnIndex(DbOpenHelper.QUESTIONS_SERVER_ID)) + "' AND " +
                    DbOpenHelper.ANSWER + " = '" + cursor.getString(cursor.getColumnIndex(DbOpenHelper.ANSWER)) + "'";

            Cursor cursorInside = sqLiteDatabase.rawQuery(query, null);
            if (!cursorInside.moveToNext()) {
                setStatus(id, tableName, columnName);
                cursorInside.close();
                break;
            } else {
                if (count == 0)
                    return true;
            }
        }

        cursor.close();
        return false;
    }

    // Set status zero
    public void setStatus(int id, String tableName, String columnName) {
        ContentValues values = new ContentValues();
        values.put(DbOpenHelper.STATUS, 1);
        sqLiteDatabase.update(tableName, values, columnName + " = ?",
                new String[]{String.valueOf(id)});
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
        question.setQuestion_server_id(cursor.getString(cursor.getColumnIndex(DbOpenHelper.QUESTIONS_SERVER_ID)));
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
        validation.setServer_validation(cursorInside.getInt(cursorInside.getColumnIndex(DbOpenHelper.SERVER_VALIDATION)));
        validation.setValidation_type(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.VALIDATION_TYPE)));
        validation.setRegex(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.REGEX)));
        validation.setError_message(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.ERROR_MESSAGE)));
        question.setValidation(validation);

        // Get Options
        query = "SELECT * FROM " +
                DbOpenHelper.OPTIONS_TABLE_NAME + " WHERE " +
                DbOpenHelper.QUESTIONS_ID + " = " + question.getQuestion_id();
        cursorInside = sqLiteDatabase.rawQuery(query, null);
        ArrayList<Option> options = new ArrayList<>();
        Option option;

        while (cursorInside.moveToNext()) {
            option = new Option();
            option.setLabel(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.LABEL)));
            option.setValue(cursorInside.getString(cursorInside.getColumnIndex(DbOpenHelper.VALUE)));

            options.add(option);
        }
        question.setOptions(options);
        cursorInside.close();

        questions.add(question);
    }

    public void insertAnswer(Page page) {
        ContentValues values = new ContentValues();
        for (Question question : page.getQuestions()) {
            values.put(DbOpenHelper.ANSWER, question.getAnswer());

            sqLiteDatabase.update(DbOpenHelper.QUESTIONS_TABLE_NAME, values, DbOpenHelper.QUESTIONS_ID + " = ?",
                    new String[]{String.valueOf(question.getQuestion_id())});

            values.clear();
        }

        String query = "SELECT * FROM " + DbOpenHelper.PAGE_TABLE_NAME + " WHERE " +
                DbOpenHelper.PAGE_ID + " = " + page.getPage_id() + " AND " +
                DbOpenHelper.CREATED_TIME_STAMP + " = 'Empty'";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        values.put(DbOpenHelper.STATUS, 1);
        if (cursor.moveToNext()) {
            values.put(DbOpenHelper.CREATED_TIME_STAMP, page.getTimeStamp());
        }
        values.put(DbOpenHelper.MODIFIED_TIME_STAMP, page.getTimeStamp());
        sqLiteDatabase.update(DbOpenHelper.PAGE_TABLE_NAME, values, DbOpenHelper.PAGE_ID + " = ?",
                new String[]{String.valueOf(page.getPage_id())});

        cursor.close();
    }
}