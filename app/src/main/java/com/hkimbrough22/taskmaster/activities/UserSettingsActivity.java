package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.hkimbrough22.taskmaster.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        int userTeamIndex = getSpinnerIndex(userSettingsTeam, teamName);
        userSettingsTeam.setSelection(userTeamIndex);

        Spinner teamSpinner = findViewById(R.id.userSettingsTeamSpinner);

        CompletableFuture<List<Team>> teamCompletableFuture = new CompletableFuture<>();
        //finding teams in DB for spinner selections
        List<Team> teams = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    if (success.hasData()) {
                        for (Team team : success.getData()) {
                            teams.add(team);
                        }
                        teamCompletableFuture.complete(teams);
                    }
//                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
                },
                failure -> {
                    Log.i(TAG, "failed");
                    teamCompletableFuture.complete(null);
                }
        );

        List<Team> teams1 = null;
        try {
            teams1 = teamCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.i(TAG, "InterruptedException while getting team:" + ie.getMessage());
        } catch (ExecutionException ee) {
            Log.i(TAG, "ExecutionException while getting team:" + ee.getMessage());
        }

        List<String> teamNames = new ArrayList<>();
        for(Team team : teams1){
            teamNames.add(team.getName());
        }
        teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
//        List<Team> teams2 = teams1;


        Button userSettingsSaveButton = findViewById(R.id.userSettingsSaveButton);
        userSettingsSaveButton.setOnClickListener(view -> {
            String username2 = userSettingsUsername.getText().toString();
            String teamName2 = userSettingsTeam.getSelectedItem().toString();

            sharedPrefEditor.putString(USER_USERNAME_KEY, username2);
            sharedPrefEditor.putString(USER_TEAM_KEY, teamName2);
            sharedPrefEditor.apply();
            Toast.makeText(this, R.string.submitted, Toast.LENGTH_SHORT).show();
            Intent mainActivityIntent = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
//            Log.i(TAG, username2);
        });
    }

    public int getSpinnerIndex(Spinner spinner, String selection) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selection)) {
                return i;
            }
        }
        return 0;
    }
}