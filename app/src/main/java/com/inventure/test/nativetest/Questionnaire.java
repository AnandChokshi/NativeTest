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
                storeAnswer(page);
                restartActivity();
            }
        });
        linearLayout.addView(next);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Method to restart activity
    private void restartActivity() {
        finish();
        startActivity(restartActivity);
    }

    // Method to store answer
    private void storeAnswer(Page page) {
        dbDataSource.insertAnswer(page);
        dbDataSource.close();
    }
}