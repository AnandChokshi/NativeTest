package com.inventure.test.nativetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Page;


public class Questionnaire extends Activity {

    // primary variables
    private DbDataSource dbDataSource;
    private Intent restartActivity;
    private Page page;
    private TextView questionView;
    private int type;
    // ID to maintain page view, without this on every resume call new view will be added
    private int id = -1;
    private LinearLayout linearLayout;
    private Button next;

    // Variables for different options
    private RadioGroup radioGroup;
    private EditText editText;
    private CheckBox[] checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        restartActivity = getIntent();
        linearLayout = (LinearLayout) findViewById(R.id.linearID);


        // get first unanswered question
        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        page = dbDataSource.readPage();

    }

    @Override
    protected void onResume() {
        super.onResume();

//        // Show UI based on type of the question
//        if (id != question.getId()) {
//            id = question.getId();
//
//            // Set View of the activity based on the type
//            switch (type) {
//                case QuestionType.RADIO:
//                    int id_counter_radio = 1;
//
//                    // Initialize RadioGroup
//                    radioGroup = new RadioGroup(this);
//                    radioGroup.setOrientation(RadioGroup.VERTICAL);
//
//                    ArrayList<String> radioButtonOptions = question.getOptions();
//
//                    // Create radioButtons based on options
//                    for (String radio : radioButtonOptions) {
//                        RadioButton radioTemp = new RadioButton(this);
//                        radioTemp.setText(radio);
//                        radioTemp.setId(id_counter_radio * 100);
//                        if (id_counter_radio == 1)
//                            radioTemp.setChecked(true);
//
//                        radioGroup.addView(radioTemp);
//                        id_counter_radio++;
//                    }
//                    linearLayout.addView(radioGroup);
//                    break;
//                case QuestionType.EDIT_TEXT:
//                    // Initialize EditText
//                    editText = new EditText(this);
//                    linearLayout.addView(editText);
//                    break;
//                case QuestionType.CHECKBOX:
//                    ArrayList<String> checkBoxOptions = question.getOptions();
//                    // Initialize CheckBoxes
//                    checkBoxes = new CheckBox[checkBoxOptions.size()];
//                    CheckBox temp;
//                    for (int i = 0; i < checkBoxOptions.size(); i++) {
//                        temp = new CheckBox(this);
//                        temp.setText(checkBoxOptions.get(i));
//                        checkBoxes[i] = temp;
//                        linearLayout.addView(temp);
//                    }
//                    break;
//            }
//
//            createNext();
//        }
    }

    // Method to define behaviour of next button based on type
    private void createNext() {
//        next = new Button(this);
//        next.setText("NEXT");
//
//        // Set behaviour of next button based on type
//        switch (type) {
//            case QuestionType.RADIO:
//                next.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int id = radioGroup.getCheckedRadioButtonId();
//                        RadioButton temp = (RadioButton) findViewById(id);
//                        storeAnswer(temp.getText().toString());
//                        restartActivity();
//                    }
//                });
//                break;
//            case QuestionType.EDIT_TEXT:
//                next.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        storeAnswer(editText.getText().toString());
//                        restartActivity();
//                    }
//                });
//                break;
//            case QuestionType.CHECKBOX:
//                next.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        StringBuilder sb = new StringBuilder();
//                        for (CheckBox checkBox : checkBoxes) {
//                            if (checkBox.isChecked())
//                                sb.append(checkBox.getText() + ":");
//                        }
//                        sb.deleteCharAt(sb.lastIndexOf(":"));
//                        storeAnswer(sb.toString());
//                        restartActivity();
//                    }
//                });
//                break;
//        }
//
//        linearLayout.addView(next);
    }

    // Method to restart activity
    private void restartActivity() {
        finish();
        startActivity(restartActivity);
    }

    // Method to store answer
    private void storeAnswer(String answer) {
      //  dbDataSource.insertAnswer(id, answer);
        dbDataSource.close();
    }
}