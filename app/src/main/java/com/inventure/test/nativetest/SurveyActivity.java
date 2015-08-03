package com.inventure.test.nativetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.inventure.test.nativetest.db.DbDataSource;
import com.inventure.test.nativetest.db.DbOpenHelper;
import com.inventure.test.nativetest.model.Page;
import com.inventure.test.nativetest.model.Question;
import com.inventure.test.nativetest.util.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;


public class SurveyActivity extends AppCompatActivity {

    // primary variables
    private DbDataSource dbDataSource;
    private JSONArray data;
    private Page page;
    private LinearLayout linearLayout;
    private UIHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        linearLayout = (LinearLayout) findViewById(R.id.linearID);
        data = new JSONArray();

        dbDataSource = new DbDataSource(this);
        dbDataSource.open();
        loadQuestionnaire();
    }

    // Load questionnaire
    private void loadQuestionnaire() {
        page = dbDataSource.readPage();
        if (page.getQuestions() == null) {
            // TODO: check confirmation if set then go to review screen otherwise send data to server
            Toast.makeText(this, "End of page", Toast.LENGTH_LONG).show();

//            SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
//            final String session_id = prefs.getString("id", null);
//            if (session_id != null) {
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        HTTPNoCompression httpNoCompression = new HTTPNoCompression();
//                        JSONObject test = httpNoCompression.sendDataToInventure(data.toString(), "http://devapi.inventureaccess.com/android/save?session_id=" + session_id);
//                        Log.d("TEST", test.toString());
//                    }
//                });
//                thread.start();
//            }
        } else {
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
                        addToJsonData();
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

    // Method to build json data
    private void addToJsonData() {
        JSONObject answer;
        try {
            for (Question question : page.getQuestions()) {
                answer = new JSONObject();
                answer.put(DbOpenHelper.QUESTIONS_SERVER_ID, question.getQuestion_server_id());
                answer.put(DbOpenHelper.ANSWER, question.getAnswer());
                data.put(answer);
            }
        } catch (JSONException e) {
            Log.d("TEST", e.getMessage());
        }
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