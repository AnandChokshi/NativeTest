package com.inventure.test.nativetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Section;
import com.inventure.test.nativetest.util.UIHelper;

import java.sql.Timestamp;
import java.util.Date;


public class QuestionnaireActivity extends AppCompatActivity {

    // primary variables
    private DbDataSource dbDataSource;
    private Page page;
    private Section section;
    private LinearLayout linearLayout;
    private UIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        linearLayout = (LinearLayout) findViewById(R.id.linearID);

        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        section = dbDataSource.readSection();
        loadQuestionnaire();
    }

    // Load questionnaire
    private void loadQuestionnaire() {
        section.setPages(dbDataSource.readPage(section.getSection_id()));

        // if no more page then send data to server or show review activity if confirm flag is set
        if (section.getPages().size() == 0) {
            if (section.getConfirmation() == 1) {
                // start review activity and finish this activity
                Log.d("TEST", "REVIEW START");
            } else {
                // send data to server directly without review screen
                Log.d("TEST", "DATA SENT TO SERVER");
            }

            // This block of code is for test------------------------------------------------
            finish();
            startActivity(new Intent(this, QuestionnaireActivity.class));
            //-------------------------------------------------------------------------------
        } else {
            page = section.getPages().get(0);
            uiHelper = new UIHelper(this, page.getQuestions(), linearLayout);

            linearLayout.removeAllViews();
            uiHelper.loadView();

            // add and set next button
            Button next = new Button(this);
            next.setText("NEXT");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uiHelper.validationCheck()) {
                        page.setQuestions(uiHelper.getAnswers());
                        page.setTimeStamp(new Timestamp(new Date().getTime()).toString());
                        storeAnswer(page);
                        loadQuestionnaire();
                    }
                }
            });
            linearLayout.addView(next);
        }
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