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
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = (Button) findViewById(R.id.addTaskSubmitButton);
        addTaskButton.setOnClickListener(view -> {

            EditText taskTitle = findViewById(R.id.addTaskTitleEditText);
            EditText taskBody = findViewById(R.id.addTaskDescriptionEditText);
            EditText taskStatus = findViewById(R.id.addTaskStateEditText);
            Spinner teamSpinner = findViewById(R.id.addTaskTeamSpinner);
            String teamSpinnerString = teamSpinner.getSelectedItem().toString();
            // take teamString and do query to DB for team
            // take team and use that for taskbuilder below
            CompletableFuture<Team> teamCompletableFuture = new CompletableFuture<>();

            List<String> teamNames = new ArrayList<>();
            Amplify.API.query(
                    ModelQuery.list(Team.class),
                    success -> {
                        Team selectedTeam = null;
                        if(success.hasData()){
                            for(Team team : success.getData()){
                                if(team.getName().equals(teamSpinnerString)){
                                    selectedTeam = team;
                                }
                                teamNames.add(team.getName());
                            }
                        }
//                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
                    },
                    failure -> {
                        Log.i(TAG, "failed");
                    }
            );

            teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));

//            String taskStatusLower = taskStatus.getText().toString().toLowerCase();
//            if (!taskStatusLower.equals("new") ||
//                    !taskStatusLower.equals("in progress") ||
//                    !taskStatusLower.equals("complete") ||
//                    !taskStatusLower.equals("unknown")) {
//                Toast.makeText(this, R.string.invalidStatus, Toast.LENGTH_LONG).show();
//            } else {
                Task newTask = Task.builder()
                        .title(taskTitle.getText().toString())
                        .team()
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
//            }
        });
    }

    public int getSpinnerIndex(Spinner spinner, String selection){
        for(int i=0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selection)){
                return i;
            }
        }
    }
}