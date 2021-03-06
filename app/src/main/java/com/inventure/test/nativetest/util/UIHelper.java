package com.inventure.test.nativetest.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inventure.test.nativetest.model.Option;
import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.model.Validation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // TODO: handle server validation
    // TODO: Load the answer and defualt if exists implement after review page so test become easy

    public void loadView() {
        int position = 0;
        for (Question question : questions) {
            makeTextView(question, linearLayout);
            switch (question.getType()) {
                case QuestionType.TEXT_BOX:
                    makeTextBox(linearLayout, position, question.getType());
                    break;
                case QuestionType.TEXT_AREA:
                    makeTextBox(linearLayout, position, question.getType());
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
                case QuestionType.TIMEPICKER:
                    makeTimePicker(linearLayout, position);
                    break;
                case QuestionType.SPINNER:
                    makeSpinner(question, linearLayout, position);
                    break;
            }
            position++;
        }
    }

    private void makeTextView(Question question, LinearLayout linearLayout) {
        //Initialize TextView
        if (question.getLabel() != null && !question.getLabel().equals("null")) {
            TextView textView = new TextView(context);
            textView.setText(question.getLabel());
            linearLayout.addView(textView);
        }
    }

    private void makeTextBox(LinearLayout linearLayout, int position, String type) {
        //Initialize EditText
        EditText editText = new EditText(context);
        editText.setLongClickable(false);

        String validation_type = questions.get(position).getValidation().getValidation_type();
        try {
            editText.setInputType(Integer.parseInt(validation_type));
        } catch (NumberFormatException e) {
            Log.d("TEST", e.getMessage());
        }
        // set properties according to Edit text type
        if (type.equals("textbox"))
            editText.setSingleLine(true);
        CustomTextWatcher customTextWatcher = new CustomTextWatcher(position);
        if (!questions.get(position).getAnswer().equals("Empty"))
            editText.setText(questions.get(position).getAnswer());
        editText.addTextChangedListener(customTextWatcher);
        linearLayout.addView(editText);
    }

    private void makeRadio(Question question, LinearLayout linearLayout, int position) {
        int id_counter_radio = 1;

        // Initialize RadioGroup
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        ArrayList<Option> radioButtonOptions = question.getOptions();

        // Create radioButtons based on options
        for (Option radio : radioButtonOptions) {
            RadioButton radioTemp = new RadioButton(context);
            radioTemp.setText(radio.getLabel());
            radioTemp.setId(id_counter_radio * 100);
            if (question.getAnswer().equals(radio.getValue()))
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
        ArrayList<Option> checkBoxOptions = question.getOptions();
        // Initialize CheckBoxes
        CheckBox temp;
        Option option;
        CustomCheckboxChangedListener customCheckboxChangedListener;
        for (int i = 0; i < checkBoxOptions.size(); i++) {
            option = checkBoxOptions.get(i);
            customCheckboxChangedListener = new CustomCheckboxChangedListener(position, option);
            temp = new CheckBox(context);
            temp.setText(option.getLabel());
            temp.setOnCheckedChangeListener(customCheckboxChangedListener);
            getCheckedCheckbox(position, option, temp.isChecked());
            if (question.getAnswer().contains(option.getValue()))
                temp.setChecked(true);
            linearLayout.addView(temp);
        }
    }

    private void makeDatePicker(LinearLayout linearLayout, int position) {
        EditText selectDate = new EditText(context);
        selectDate.setFocusable(false);

        CustomDateSetListener customDateSetListener = new CustomDateSetListener(position, selectDate);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, customDateSetListener,
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        CustomDateOnClickListener customDateOnClickListener = new CustomDateOnClickListener(datePickerDialog);
        selectDate.setOnClickListener(customDateOnClickListener);

        linearLayout.addView(selectDate);
    }

    private void makeTimePicker(LinearLayout linearLayout, int position) {
        EditText selectTime = new EditText(context);
        selectTime.setFocusable(false);

        CustomTimeSetListener customTimeSetListener = new CustomTimeSetListener(position, selectTime);
        Calendar newTime = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, customTimeSetListener,
                newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE), true);

        CustomTimeOnClickListener customTimeOnClickListener = new CustomTimeOnClickListener(timePickerDialog);
        selectTime.setOnClickListener(customTimeOnClickListener);

        linearLayout.addView(selectTime);
    }

    private void makeSpinner(Question question, LinearLayout linearLayout, int position) {
        Spinner spinner = new Spinner(context);
        CustomOnItemSelectedListener customOnItemSelectedListener = new CustomOnItemSelectedListener(position);
        spinner.setOnItemSelectedListener(customOnItemSelectedListener);

        ArrayAdapter<Option> arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, question.getOptions());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        linearLayout.addView(spinner);
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
        checkedId = checkedId / 100;
        if (radioButton != null) {
            questions.get(position).setAnswer(questions.get(position).getOptions().get(checkedId - 1).getValue());
        }
    }

    // To Store answer of Checkbox
    private class CustomCheckboxChangedListener implements CheckBox.OnCheckedChangeListener {
        private int position;
        private Option option;

        private CustomCheckboxChangedListener(int position, Option option) {
            this.position = position;
            this.option = option;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            getCheckedCheckbox(position, option, isChecked);
        }
    }

    // To call the date picker
    private class CustomDateOnClickListener implements EditText.OnClickListener {
        DatePickerDialog datePickerDialog;

        private CustomDateOnClickListener(DatePickerDialog datePickerDialog) {
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

    // To call the time picker
    private class CustomTimeOnClickListener implements EditText.OnClickListener {
        TimePickerDialog timePickerDialog;

        private CustomTimeOnClickListener(TimePickerDialog timePickerDialog) {
            this.timePickerDialog = timePickerDialog;
        }

        @Override
        public void onClick(View v) {
            timePickerDialog.show();
        }
    }

    // Time picker custom listener
    private class CustomTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        private int position;
        private EditText editText;

        private CustomTimeSetListener(int position, EditText editText) {
            this.position = position;
            this.editText = editText;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editText.setText(String.valueOf(hourOfDay + ":" + minute));
            questions.get(position).setAnswer(String.valueOf(hourOfDay + ":" + minute));
        }
    }

    // Get the checked checkbox value
    private void getCheckedCheckbox(int position, Option option, boolean isChecked) {
        String value = option.getValue();
        if (isChecked) {
            sb.append("(" + value + ")");
        } else {
            if (sb.indexOf(value) != -1) {
                sb.delete(sb.indexOf(value) - 1, sb.indexOf(value) + value.length() + 1);
            }
        }
        questions.get(position).setAnswer(sb.toString());
    }

    private class CustomOnItemSelectedListener implements Spinner.OnItemSelectedListener {
        int position;

        private CustomOnItemSelectedListener(int position) {
            this.position = position;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Option option = (Option) parent.getItemAtPosition(position);
            questions.get(this.position).setAnswer(option.getValue());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    // Check required conditions
    public boolean validationCheck() {
        Validation validation;
        Pattern pattern;
        Matcher matcher;
        for (Question question : questions) {
            validation = question.getValidation();
            if (validation.getRequired() == 1) {
                if (question.getAnswer().equals("") || question.getAnswer().equals("Empty")) {
                    Toast.makeText(context, validation.getError_message(), Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            if (validation.getRegex() != null && !validation.getRegex().equals("null")) {
                pattern = Pattern.compile(validation.getRegex());
                matcher = pattern.matcher(question.getAnswer());
                if (!matcher.matches()) {
                    Toast.makeText(context, question.getLabel() + " Invalid Input", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }

        return true;
    }

    // To get the answers
    public ArrayList<Question> getAnswers() {
        return questions;
    }
}