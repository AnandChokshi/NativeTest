package com.inventure.test.nativetest.util;

import android.content.Context;
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

    public UIHelper(Context context) {
        this.context = context;
    }

    public void loadView(LinearLayout linearLayout, ArrayList<Question> questions) {
        for (Question question : questions) {
            makeTextView(question, linearLayout);
            switch (question.getType()) {
                case "textbox":
                    makeTextBox(question, linearLayout);
                    break;
                case "radio":
                    makeRadio(question, linearLayout);
                    break;
            }
        }
    }

    private void makeTextView(Question question, LinearLayout linearLayout) {
        //Initialize TextView
        TextView textView = new TextView(context);
        textView.setText(question.getLabel());
        linearLayout.addView(textView);
    }

    private void makeTextBox(Question question, LinearLayout linearLayout) {
        //Initialize EditText
        EditText editText = new EditText(context);
        linearLayout.addView(editText);
    }

    private void makeRadio(Question question, LinearLayout linearLayout) {
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
}