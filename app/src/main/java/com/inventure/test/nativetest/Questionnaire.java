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
import com.inventure.test.nativetest.model.Section;
import com.inventure.test.nativetest.util.UIHelper;


public class Questionnaire extends Activity {

    // primary variables
    private DbDataSource dbDataSource;
    private Intent restartActivity;
    private Page page;
    private Section section;
    private LinearLayout linearLayout;
    private UIHelper uiHelper;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        restartActivity = getIntent();
        linearLayout = (LinearLayout) findViewById(R.id.linearID);

        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        section = dbDataSource.readSection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (uiHelper != null) {
            section.setPages(dbDataSource.readPage(section.getSection_id()));
        }

        if (section.getPages().size() == 0) {
            // if page array is null set status of section null
            // Check whether it has confirmation flag set
            // and load next section by restarting the activity
        } else {
            page = section.getPages().get(0);
            uiHelper = new UIHelper(this, page.getQuestions(), linearLayout);

            linearLayout.removeAllViews();
            uiHelper.loadView();

            // add and set next button
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
                    onResume();
                }
            });
            linearLayout.addView(next);
        }
    }

    // Method to restart activity
    private void restartActivity() {
        dbDataSource.close();
        finish();
        startActivity(restartActivity);
    }

    // Method to store answer
    private void storeAnswer(Page page) {
        dbDataSource.insertAnswer(page);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbDataSource.close();
    }
}