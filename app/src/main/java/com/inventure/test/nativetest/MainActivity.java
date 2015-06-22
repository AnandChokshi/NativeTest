package com.inventure.test.nativetest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.inventure.test.nativetest.parser.JsonReader;
import com.inventure.test.nativetest.testUtil.JsonCreator;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends Activity {

    private CallbackManager callbackManager;
    LoginButton loginFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // Define Login Button behaviour
        loginFB = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent questionnaire = new Intent(getBaseContext(), Questionnaire.class);
                startActivity(questionnaire);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //Reads data from test json and load it into database
        try {
            JSONArray jsonArray = JsonCreator.getJsonArray();
            JsonReader jsonReader = new JsonReader(this);
            jsonReader.readDataFromJSON(jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Facebook Login Button Call Back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}