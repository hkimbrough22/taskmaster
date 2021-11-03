package com.hkimbrough22.taskmaster.models;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.TAG;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class TaskmasterApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        try{
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            Log.e(TAG, "Error initializing Amplify" + e.getMessage(), e);
        }
    }
}
