package com.inventure.test.nativetest.util;

import android.content.Context;
import android.content.Intent;

import com.inventure.test.nativetest.AccountActivity;
import com.inventure.test.nativetest.Congratulation;
import com.inventure.test.nativetest.SurveyActivity;

/**
 * Created by Anand on 7/27/2015.
 */
public class ServerDecision {

    public static void launchActivity(String type, Context context) {
        Intent startNewActivity;
        switch (type) {
            case ActivityTypes.SURVEY:
                // TODO: Compare the old data with new json if exists in DB
                // TODO: store the hash of the json in DB
                startNewActivity = new Intent(context, SurveyActivity.class);
                context.startActivity(startNewActivity);
                break;
            case ActivityTypes.ACCOUNT:
                startNewActivity = new Intent(context, AccountActivity.class);
                context.startActivity(startNewActivity);
                break;
            case ActivityTypes.GENERAL_PURPOSE:
                break;
            case ActivityTypes.CONGRATULATION:
                startNewActivity = new Intent(context, Congratulation.class);
                context.startActivity(startNewActivity);
                break;
        }
    }
}
