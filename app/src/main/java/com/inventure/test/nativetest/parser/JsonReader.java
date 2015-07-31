package com.inventure.test.nativetest.parser;

import android.content.Context;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Condition;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.model.Validation;
import com.inventure.test.nativetest.util.QuestionType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anand on 6/16/2015.
 */
public class JsonReader {
    private Context context;
    private ArrayList<Page> pages;

    public JsonReader(Context context) {
        this.context = context;
    }

    /*
    Name of the tags are hardcoded right now for ease of use
    as this is not the final version
     */
    public void readDataFromJSON(JSONArray jsonArray) throws JSONException {
        pages = new ArrayList<>();
        Page page;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            page = new Page();

            page.setConditions(readFromConditionJson(object.getJSONArray("condition")));
            page.setQuestions(readFromQuestionJSON(object.getJSONArray("questions")));

            pages.add(page);
        }

        insertInDatabase();
    }

    // Read Condition of the page
    private ArrayList<Condition> readFromConditionJson(JSONArray jsonArray) throws JSONException {
        Condition condition;
        ArrayList<Condition> conditions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            condition = new Condition();
            condition.setQid(jsonArray.getJSONObject(i).getString("qid"));
            condition.setAnswer(jsonArray.getJSONObject(i).getString("answer"));
            conditions.add(condition);
        }
        return conditions;
    }

    // Read all questions from the page
    private ArrayList<Question> readFromQuestionJSON(JSONArray jsonArray) throws JSONException {
        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            Question question = new Question();
            JSONObject object = jsonArray.getJSONObject(i);
            String type = object.getString("type");

            // TO DO: do something for plaintext
            if (type.equals("plainText"))
                continue;

            question.setType(type);
            question.setQuestion_server_id(object.getString("question_server_id"));
            question.setLabel(object.getString("label"));

            if (type.equals(QuestionType.TEXT_BOX) || type.equals(QuestionType.TEXT_AREA))
                question.setPlaceHolder(object.getString("placeholder"));

            if (object.getJSONArray("default").length() > 0) {
                question.setDefaultValues(readDefaultValues(object.getJSONArray("default")));
            }

            question.setValidation(readValidationValues(object.getJSONObject("validation")));

            if (type.equals(QuestionType.RADIO) || type.equals(QuestionType.SPINNER) || type.equals(QuestionType.CHECKBOX))
                question.setOptions(readFromOptions(object.getJSONArray("options")));

            questions.add(question);
        }

        return questions;
    }

    // Read default values
    private ArrayList<String> readDefaultValues(JSONArray jsonArray) throws JSONException {
        ArrayList<String> defaultValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            defaultValues.add(jsonArray.getString(i));
        }

        return defaultValues;
    }

    // Read Validation data
    private Validation readValidationValues(JSONObject jsonObject) throws JSONException {
        Validation validation = new Validation();
        validation.setRequired(jsonObject.getInt("required"));
        validation.setServer_validation(jsonObject.getInt("server_validation"));
        validation.setValidation_type(jsonObject.getString("type"));
        validation.setRegex(jsonObject.getString("regex"));
        validation.setError_message(jsonObject.getString("error_message"));

        return validation;
    }

    // Read Options data
    private ArrayList<String> readFromOptions(JSONArray jsonArray) throws JSONException {
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            // TODO: take value and label from json it's temp to take value just to see code works
            options.add(jsonArray.getJSONObject(i).getString("value"));
        }

        return options;
    }

    private void insertInDatabase() {
        DbDataSource dbDataSource = new DbDataSource(context);
        dbDataSource.open();
        dbDataSource.insertJSON(pages);
        dbDataSource.close();
    }
}