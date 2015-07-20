package com.inventure.test.nativetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.db.DbOpenHelper;
import com.inventure.test.nativetest.model.Page;
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
            dbDataSource.setStatus(section.getSection_id(), DbOpenHelper.SECTION_TABLE_NAME, DbOpenHelper.SECTION_ID);
            if (section.getConfirmation() == 1) {
                // start review activity here
            } else {
                restartActivity();
            }
            // To TEST => after implementing review screen remove line of code after this
            restartActivity();
            // Remove above code after review screen
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
                    if (uiHelper.validationCheck()) {
                        page.setQuestions(uiHelper.getAnswers());
                        storeAnswer(page);
                        onResume();
                    }
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