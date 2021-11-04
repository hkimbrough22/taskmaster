package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hkimbrough22.taskmaster.R;

public class UserSettingsActivity extends AppCompatActivity {

    public final static String TAG = "hkim_usersettingsactivity";
    public final static String USER_USERNAME_KEY = "userUsername";
    public final static String USER_TEAM_KEY = "userTeamName";
    public static SharedPreferences sharedPref;
    public static SharedPreferences.Editor sharedPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefEditor = sharedPref.edit();
        EditText userSettingsUsername = findViewById(R.id.userSettingsUsernameEditText);
        Spinner userSettingsTeam = findViewById(R.id.userSettingsTeamSpinner);
        String username = sharedPref.getString(USER_USERNAME_KEY, "");
        String teamName = sharedPref.getString(USER_TEAM_KEY, "");
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