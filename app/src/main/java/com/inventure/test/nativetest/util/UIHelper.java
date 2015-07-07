package com.inventure.test.nativetest.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inventure.test.nativetest.model.Question;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Anand on 6/22/2015.
 */
public class UIHelper {
    private Context context;
    private ArrayList<Question> questions;
    private LinearLayout linearLayout;
    private StringBuilder sb;

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
                case QuestionType.TEXT_BOX:
                    makeTextBox(linearLayout, position);
                    break;
                case QuestionType.RADIO:
                    makeRadio(question, linearLayout, position);
                    break;
                case QuestionType.CHECKBOX:
                    makeCheckBox(question, linearLayout, position);
                    break;
                case QuestionType.DATEPICKER:
                    makeDatePicker(linearLayout, position);
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
        CustomTextWatcher customTextWatcher = new CustomTextWatcher(position);
        editText.addTextChangedListener(customTextWatcher);
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
        CustomRadioChangeListener customRadioChangeListener = new CustomRadioChangeListener(position);
        radioGroup.setOnCheckedChangeListener(customRadioChangeListener);
        linearLayout.addView(radioGroup);
        getCheckRadioButton(position, radioGroup, radioGroup.getCheckedRadioButtonId());
    }

    private void makeCheckBox(Question question, LinearLayout linearLayout, int position) {
        sb = new StringBuilder();
        ArrayList<String> checkBoxOptions = question.getOptions();
        // Initialize CheckBoxes
        CheckBox temp;
        String value;
        CustomCheckboxChangedListener customCheckboxChangedListener;
        for (int i = 0; i < checkBoxOptions.size(); i++) {
            value = checkBoxOptions.get(i);
            customCheckboxChangedListener = new CustomCheckboxChangedListener(position, value);
            temp = new CheckBox(context);
            temp.setText(value);
            temp.setOnCheckedChangeListener(customCheckboxChangedListener);
            getCheckedCheckbox(position, value, temp.isChecked());
            linearLayout.addView(temp);
        }
    }

    private void makeDatePicker(LinearLayout linearLayout, final int position) {
        EditText selectDate = new EditText(context);

        CustomDateSetListener customDateSetListener = new CustomDateSetListener(position, selectDate);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, customDateSetListener, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        CustomOnClickListener customOnClickListener = new CustomOnClickListener(datePickerDialog);
        selectDate.setOnClickListener(customOnClickListener);

        linearLayout.addView(selectDate);
    }

    // To store the answers of edit box
    private class CustomTextWatcher implements TextWatcher {
        private int position;

        private CustomTextWatcher(int position) {
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

    // To store answer of radio group
    private class CustomRadioChangeListener implements RadioGroup.OnCheckedChangeListener {
        private int position;

        private CustomRadioChangeListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            getCheckRadioButton(position, group, checkedId);
        }
    }

    // Get the checked radio button ID
    private void getCheckRadioButton(int position, RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        questions.get(position).setAnswer(radioButton.getText().toString());
    }

    // To Store answer of Checkbox
    private class CustomCheckboxChangedListener implements CheckBox.OnCheckedChangeListener {
        private int position;
        private String value;

        private CustomCheckboxChangedListener(int position, String value) {
            this.position = position;
            this.value = value;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getCheckedCheckbox(position, value, isChecked);
        }
    }

    // To call the date picker
    private class CustomOnClickListener implements EditText.OnClickListener {
        DatePickerDialog datePickerDialog;

        private CustomOnClickListener(DatePickerDialog datePickerDialog) {
            this.datePickerDialog = datePickerDialog;
        }

        @Override
        public void onClick(View v) {
            datePickerDialog.show();
        }
    }

    // Date picker custom listener
    private class CustomDateSetListener implements DatePickerDialog.OnDateSetListener {
        private int position;
        private EditText editText;

        private CustomDateSetListener(int position, EditText editText) {
            this.position = position;
            this.editText = editText;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            editText.setText(dateFormatter.format(newDate.getTime()));
            questions.get(position).setAnswer(dateFormatter.format(newDate.getTime()));
        }
    }

    // Get the checked checkbox value
    private void getCheckedCheckbox(int position, String value, boolean isChecked) {
        if (isChecked) {
            sb.append("(" + value + ")");
        } else {
            if (sb.indexOf(value) != -1) {
                sb.delete(sb.indexOf(value) - 1, sb.indexOf(value) + value.length() + 1);
            }
        }
        questions.get(position).setAnswer(sb.toString());
    }

    // To get the answers
    public ArrayList<Question> getAnswers() {
        return questions;
    }
}