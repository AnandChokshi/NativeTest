package com.inventure.test.nativetest.parser;

import android.content.Context;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Question;
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
    private DbDataSource dbDataSource;
    private ArrayList<Question> questions;

    public JsonReader(Context context) {
        this.context = context;
    }

    public void readDataFromJSON(JSONArray jsonArray) throws JSONException {
        int type;
        questions = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Question question = new Question();

            question.setId(object.getInt("index"));
            question.setQuestion(object.getString("question"));

            type = object.getInt("type");
            question.setType(type);

            if (type == QuestionType.RADIO || type == QuestionType.CHECKBOX) {
                JSONArray options = object.getJSONArray("options");
                ArrayList<String> optionList = new ArrayList<>();
                for (int j = 0; j < options.length(); j++) {
                    optionList.add(options.getJSONObject(j).getString("option"));
                }
                question.setOptions(optionList);
            }

            questions.add(question);
        }
        insertInDatabase();
    }

    private void insertInDatabase() {
        dbDataSource = new DbDataSource(context);
        dbDataSource.open();
        dbDataSource.insertQuestions(questions);
        dbDataSource.close();
    }
}
