package com.inventure.test.nativetest.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inventure.test.nativetest.model.Question;

import java.util.ArrayList;

/**
 * Created by Anand on 6/22/2015.
 */
public class UIHelper {
    private Context context;
    private ArrayList<Question> questions;
    private LinearLayout linearLayout;

    public UIHelper(Context context, ArrayList<Question> questions, LinearLayout linearLayout) {
        this.context = context;
        this.questions = questions;
        this.linearLayout = linearLayout;
    }

    public void loadView() {
        int position = 0;
        for (Question question : questions) {
            makeTextView(question, linearLayout);
            switch (question.getType()) {
                case "textbox":
                    makeTextBox(linearLayout, position);
                    break;
                case "radio":
                    makeRadio(question, linearLayout, position);
                    break;
            }
            position++;
        }
    }

    private void makeTextView(Question question, LinearLayout linearLayout) {
        //Initialize TextView
        TextView textView = new TextView(context);
        textView.setText(question.getLabel());
        linearLayout.addView(textView);
    }

    private void makeTextBox(LinearLayout linearLayout, int position) {
        //Initialize EditText
        EditText editText = new EditText(context);
        MyTextWatcher myTextWatcher = new MyTextWatcher(position);
        editText.addTextChangedListener(myTextWatcher);
        linearLayout.addView(editText);
    }

    private void makeRadio(Question question, LinearLayout linearLayout, int position) {
        int id_counter_radio = 1;

        // Initialize RadioGroup
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        ArrayList<String> radioButtonOptions = question.getOptions();

        // Create radioButtons based on options
        for (String radio : radioButtonOptions) {
            RadioButton radioTemp = new RadioButton(context);
            radioTemp.setText(radio);
            radioTemp.setId(id_counter_radio * 100);
            if (id_counter_radio == 1)
                radioTemp.setChecked(true);

            radioGroup.addView(radioTemp);
            id_counter_radio++;
        }
        linearLayout.addView(radioGroup);
    }

    // To store the answers of edit box
    private class MyTextWatcher implements TextWatcher {
        private int position;

        private MyTextWatcher(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            questions.get(position).setAnswer(s.toString());
        }
    }

    // To get the answers
    public ArrayList<Question> getAnswers() {
        return questions;
    }
}