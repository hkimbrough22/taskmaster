package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.hkimbrough22.taskmaster.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {

    public final static String TAG = "hkim_addTaskActivity";
    public final static String DEFAULT_TEAM_NAME = "Team 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //Sets up spinners
        Spinner teamSpinner = findViewById(R.id.addTaskTeamSpinner);

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
        teamNames.sort(Comparator.naturalOrder());
        teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
        List<Team> teams2 = teams1;

        Button addTaskButton = findViewById(R.id.addTaskSubmitButton);
        addTaskButton.setOnClickListener(view -> {

            String selection = teamSpinner.getSelectedItem().toString();
            Team selectedTeam = null;
            for(Team team : teams2){
                if(selection.equals(team.getName())){
                    selectedTeam = team;
                }
                teamNames.add(team.getName());
            }

            EditText taskTitle = findViewById(R.id.addTaskTitleEditText);
            EditText taskBody = findViewById(R.id.addTaskDescriptionEditText);
            EditText taskStatus = findViewById(R.id.addTaskStateEditText);

            // take teamString and do query to DB for team
            // take team and use that for taskbuilder below
//            Amplify.API.query(
//                    ModelQuery.list(Team.class),
//                    success -> {
//                        if (success.hasData()) {
//                            for (Team team : success.getData()) {
//                                if (team.getName().equals(selection)) {
//                                    selectedTeam = team;
//                                }
//                            }
//                        }
////                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
//                    },
//                    failure -> {
//                        Log.i(TAG, "failed");
//                    }
//            );

            String taskStatusLower = taskStatus.getText().toString().toLowerCase();
            if (!taskStatusLower.equals("new") &&
                    !taskStatusLower.equals("in progress") &&
                    !taskStatusLower.equals("complete") &&
                    !taskStatusLower.equals("unknown")) {
                Toast.makeText(this, R.string.invalidStatus, Toast.LENGTH_LONG).show();
            } else {
            Task newTask = Task.builder()
                    .title(taskTitle.getText().toString())
                    .team(selectedTeam)
                    .body(taskBody.getText().toString())
                    .state(taskStatus.getText().toString())
                    .build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "succeeded"),
                    failure -> Log.i(TAG, "failed")
            );
            Toast.makeText(this, R.string.taskAdded, Toast.LENGTH_SHORT).show();
            Intent mainActivityIntent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
            }
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