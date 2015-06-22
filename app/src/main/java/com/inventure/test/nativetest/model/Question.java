package com.inventure.test.nativetest.model;

import java.util.ArrayList;

/**
 * Created by Anand on 6/16/2015.
 */
public class Question {

    private int id;
    private String question;
    private int type;
    private String answer;
    private ArrayList<String> options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nQuestion: " + question + "\nAnswer: " + answer;
    }
}