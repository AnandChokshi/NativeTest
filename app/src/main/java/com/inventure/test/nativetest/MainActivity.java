package com.inventure.test.nativetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import org.json.JSONObject;


public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;

    private CallbackManager callbackManager;
    LoginButton loginFB;

    // TEST CODE
    JSONObject answer;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        // Initialize Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // -------------- Facebook ----------------------
        // Define Login Button behaviour
        loginFB = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                finish();
                Intent questionnaire = new Intent(getBaseContext(), SurveyActivity.class);
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
        // ----------------- Facebook Ends ------------------

        // ----------------- Google -------------------------

        // Build GoogleApiClient with access to basic profile
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

        // ----------------- Google Ends --------------------

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String jsonToSend = "{\"data\":{\"device\":{\"android_id\":\"9822197841fadd0c\",\"device_id\":\"354068063926074\",\"version\":\"3.5\"},\"auth\":{\"data\":{\"id\":\"836043463111397\",\"first_name\":\"Bob\",\"timezone\":-7,\"email\":\"ldttestaccount3@gmail.com\",\"verified\":true,\"name\":\"Bob   Letsdo\",\"locale\":\"en_GB\",\"link\":\"https:\\/\\/www.facebook.com\\/app_scoped_user_id\\/836043463111397\\/\",\"last_name\":\"Letsdo\",\"gender\":\"male\",\"updated_time\":\"2015-03-26T19:28:26+0000\"},\"type\":\"FB\",\"app_id\":\"933613079993549\",\"access_token\":\"CAANRHbv9nM0BAKwAcWTZB784y8C7sb0ZBJDlgdiYe4eeZCdHa413LVfZBZC50buN7eR0djNYdJov2Do1lZCqv4dQIOZAp2BgDNmphHY7ZAoPRTngSTsLICsOZCwImsv5brjZBSrZBrTHJ9B0kGZAVJvqy1bHeCNejG3FfPVyO51FHbe5OjrMkKgVFQooUZCYTdtVd5KXmFtHmknF5G8dZB7p0pJ0l8\"},\"country\":\"KE\"}}";
//                    HTTPNoCompression httpNoCompression = new HTTPNoCompression();
//                    answer = httpNoCompression.sendDataToInventure(jsonToSend, "http://devapi.inventureaccess.com/android/login");
//
//                    JSONObject jsonObjectData = answer.getJSONObject("data");
//                    JSONObject jsonObjectContent = jsonObjectData.getJSONObject("content");
//
//                    JsonReader jsonReader = new JsonReader(context);
//                    jsonReader.readDataFromJSON(jsonObjectContent.getJSONArray("pages"));
//
//                    SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
//                    editor.putString("id", answer.getString("session_id"));
//                    editor.commit();
//
//                    finish();
//                    //TODO: get show confirmation out and store it in shared preference
//                    ServerDecision.launchActivity(jsonObjectData.getString("type"), context);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        thread.start();

//
//        // Reads data from test json and load it into database
//        try {
//            JSONObject jsonObjectData = JsonCreator.getJson();
//            JSONObject jsonObjectContent = jsonObjectData.getJSONObject("content");
//
//            JsonReader jsonReader = new JsonReader(this);
//            jsonReader.readDataFromJSON(jsonObjectContent.getJSONArray("pages"));
//
//            finish();
//            //TODO: get show confirmation out and store it in shared preferrence
//            ServerDecision.launchActivity(jsonObjectData.getString("type"), this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    // Facebook Login Button Call Back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    // ------------ Implemented methods ---------------- //
    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        mShouldResolve = false;
        finish();
        Intent questionnaire = new Intent(getBaseContext(), SurveyActivity.class);
        startActivity(questionnaire);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_in_button) {
            onSignInClicked();
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        mShouldResolve = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.

        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}