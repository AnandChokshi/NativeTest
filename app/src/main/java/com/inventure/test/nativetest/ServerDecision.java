package com.inventure.test.nativetest;

import android.content.Context;
import android.content.Intent;

import com.inventure.test.nativetest.util.ActivityTypes;

/**
 * Created by Anand on 7/27/2015.
 */
public class ServerDecision {

    public static void launchActivity(String type, Context context) {
        Intent startNewActivity;
        switch (type) {
            case ActivityTypes.QUESTIONNAIRE:
                startNewActivity = new Intent(context, QuestionnaireActivity.class);
                context.startActivity(startNewActivity);
                break;
            case ActivityTypes.ACCOUNT:
                startNewActivity = new Intent(context, AccountActivity.class);
                context.startActivity(startNewActivity);
                break;
            case ActivityTypes.GENERAL_PURPOSE:
                break;
        }
    }
}
