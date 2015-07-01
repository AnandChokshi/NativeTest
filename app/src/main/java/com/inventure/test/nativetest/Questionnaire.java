package com.inventure.test.nativetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.util.UIHelper;


public class Questionnaire extends Activity {

    // primary variables
    private DbDataSource dbDataSource;
    private Intent restartActivity;
    private Page page;
    private LinearLayout linearLayout;
    private UIHelper uiHelper;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        restartActivity = getIntent();
        linearLayout = (LinearLayout) findViewById(R.id.linearID);

        // Get First Unanswered Page
        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        page = dbDataSource.readPage();
        dbDataSource.close();

        uiHelper = new UIHelper(this, page.getQuestions(), linearLayout);

        uiHelper.loadView();
        next = new Button(this);
        next.setText("NEXT");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page.setQuestions(uiHelper.getAnswers());
                for (Question question : page.getQuestions()) {
                    Log.d("TEXT", question.getAnswer() + "\n");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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