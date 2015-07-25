package com.inventure.test.nativetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.db.DbOpenHelper;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Section;
import com.inventure.test.nativetest.util.UIHelper;

import java.sql.Timestamp;
import java.util.Date;


public class QuestionnaireActivity extends AppCompatActivity {

    // primary variables
    private DbDataSource dbDataSource;
    private Intent restartActivity;
    private Page page;
    private Section section;
    private LinearLayout linearLayout;
    private UIHelper uiHelper;
    private Button next;
    private boolean onNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        restartActivity = getIntent();

        // For stopping the animation of activity reloading
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //overridePendingTransition(0, 0);

        linearLayout = (LinearLayout) findViewById(R.id.linearID);

        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        section = dbDataSource.readSection();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // read next page only if next button pressed
        if (onNext) {
            onNext = false;
            // For the first time when activity starts get the pages from database
            if (uiHelper != null) {
                section.setPages(dbDataSource.readPage(section.getSection_id()));
            }

            loadQuestionnaire();
        }
    }

    // Load questionnaire
    private void loadQuestionnaire() {
        // if section's page array is null which means we are at the end of the questionnaire
        // Then start the account flow
        if (section.getPages() == null) {
            // Start account flow here
            finish();
            Intent accountActivity = new Intent(this, AccountActivity.class);
            startActivity(accountActivity);
        } else {
            // if we are not at the end of the questionnaire
            // then check if its end of the section or not
            // if it's end of the section then restart activity after changing status of section as complete
            if (section.getPages().size() == 0) {
                dbDataSource.setStatus(section.getSection_id(), DbOpenHelper.SECTION_TABLE_NAME, DbOpenHelper.SECTION_ID);
                if (section.getConfirmation() == 1) {
                    // start review activity here
                } else {
                    restartActivity();
                }

                //-------------- REMOVE THIS AFTER -------------------//
                // To TEST => after implementing review screen remove line of code after this
                restartActivity();
                // Remove above code after review screen
                //----------------------------------------------------//
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
                            page.setTimeStamp(new Timestamp(new Date().getTime()).toString());
                            storeAnswer(page);
                            onNext = true;
                            onResume();
                        }
                    }
                });
                linearLayout.addView(next);
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_zenDesk) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}