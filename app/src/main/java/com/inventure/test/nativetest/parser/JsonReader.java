package com.inventure.test.nativetest.parser;

import android.content.Context;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Condition;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.QuestionMod;
import com.inventure.test.nativetest.model.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Anand on 6/16/2015.
 */
public class JsonReader {
    private Context context;
    private DbDataSource dbDataSource;
    private ArrayList<Page> pages;

    public JsonReader(Context context) {
        this.context = context;
    }

    /*
    Name of the tags are hardcoded right now for ease of use
    later we can change it
     */
    public void readDataFromJSON(JSONArray jsonArray) throws JSONException {
        pages = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Page page = new Page();

            page.setPage_order(object.getInt("page_order"));
            page.setCondition(readFromConditionJson(object.getJSONObject("condition")));
            page.setQuestions(readFromQuestionJSON(object.getJSONArray("questions")));

            pages.add(page);
        }
        insertInDatabase();
    }

    // Read Condition of the page
    private Condition readFromConditionJson(JSONObject jsonObject) throws JSONException {
        Condition condition = new Condition();
        condition.setQid(jsonObject.getString("qid"));
        condition.setAnswer(jsonObject.getString("answer"));

        return condition;
    }

    // Read all questions from the page
    private ArrayList<QuestionMod> readFromQuestionJSON(JSONArray jsonArray) throws JSONException {
        ArrayList<QuestionMod> questionMods = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            QuestionMod questionMod = new QuestionMod();
            JSONObject object = jsonArray.getJSONObject(i);

            questionMod.setQuestion_server_id(object.getInt("question_server_id"));
            String type = object.getString("type");
            questionMod.setType(type);
            questionMod.setLabel(object.getString("label"));

            if (type.equals("textbox") || type.equals("textarea"))
                questionMod.setPlaceHolder(object.getString("placeholder"));

            if (object.getJSONArray("default").length() > 0) {
                questionMod.setDefaultValues(readDefaultValues(object.getJSONArray("default")));
            }

            questionMod.setValidation(readValidationValues(object.getJSONObject("validation")));

            if (type.equals("radio") || type.equals("spinner") || type.equals("checkbox"))
                questionMod.setOptions(readFromOptions(object.getJSONArray("options")));

            questionMods.add(questionMod);
        }

        return questionMods;
    }

    // Read default values
    private ArrayList<String> readDefaultValues(JSONArray jsonArray) throws JSONException {
        ArrayList<String> defaultValues = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            defaultValues.add(jsonArray.getJSONObject(i).getString("value"));
        }

        return defaultValues;
    }

    // Read Validation data
    private Validation readValidationValues(JSONObject jsonObject) throws JSONException {
        Validation validation = new Validation();
        validation.setRequired(jsonObject.getInt("required"));
        validation.setValidation_type(jsonObject.getString("validation_type"));
        validation.setRegex(jsonObject.getString("regex"));
        validation.setError_message(jsonObject.getString("error_message"));

        return validation;
    }

    // Read Options data
    private ArrayList<String> readFromOptions(JSONArray jsonArray) throws JSONException {
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            options.add(jsonArray.getJSONObject(i).getString("option"));
        }

        return options;
    }

    private void insertInDatabase() {
        dbDataSource = new DbDataSource(context);
        dbDataSource.open();
        dbDataSource.insertJSON(pages);
        dbDataSource.close();
    }
}