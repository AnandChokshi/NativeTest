package com.inventure.test.nativetest.model;

import java.util.ArrayList;

/**
 * Created by Anand on 6/25/2015.
 */
public class QuestionMod {
    private int question_id;
    private int question_server_id;
    private String type;
    private String label;
    private String answer;
    private String placeHolder;
    private ArrayList<String> defaultValues = new ArrayList<>();
    private Validation validation;
    private ArrayList<String> options;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getQuestion_server_id() {
        return question_server_id;
    }

    public void setQuestion_server_id(int question_server_id) {
        this.question_server_id = question_server_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public ArrayList<String> getDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(ArrayList<String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}