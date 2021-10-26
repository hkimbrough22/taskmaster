package com.hkimbrough22.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class UserSettingsActivity extends AppCompatActivity {

    public final static String TAG = "hkim_usersettingsactivity";
    public final static String USER_USERNAME_KEY = "userUsername";
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefEditor = sharedPref.edit();
        EditText userSettingsUsername = findViewById(R.id.userSettingsUsernameEditText);
        String username = sharedPref.getString(USER_USERNAME_KEY, "");
        userSettingsUsername.setText(username);

        Button userSettingsSaveButton = findViewById(R.id.userSettingsSaveButton);
        userSettingsSaveButton.setOnClickListener(view -> {
            String username2 = userSettingsUsername.getText().toString();
            sharedPrefEditor.putString(USER_USERNAME_KEY, username2);
            sharedPrefEditor.apply();
            Toast.makeText(this, R.string.submitted, Toast.LENGTH_SHORT).show();
            Intent mainActivityIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
//            Log.i(TAG, username2);
        });
    }
}