package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.USER_TEAM_KEY;
import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.USER_USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.adapters.TaskListRecyclerViewAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "hkim_taskmaster_mainActivity";
    public final static String TASK_TITLE_EXTRA_STRING = "taskTitle";
    public final static String TASK_BODY_EXTRA_STRING = "taskBody";
    public final static String TASK_STATE_EXTRA_STRING = "taskState";
    public final static String TASK_ADDED_ON_EXTRA_STRING = "taskAddedOn";
    public final static String DATABASE_NAME = "hkim_taskmaster_db";
    public final static String TEAM_UNKNOWN_NAME = "Team Unknown";

    protected static SharedPreferences sharedPref;
    protected static Resources resources;

    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;
    List<Task> userTeamTasks = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Testing S3 file upload
//        File testFile = new File(getApplicationContext().getFilesDir(), "testFileName"); //makes file on the phone
//
//        try (BufferedWriter testFileBufferedWriter = new BufferedWriter(new FileWriter(testFile))){
////            BufferedWriter testFileBufferedWriter = new BufferedWriter(new FileWriter(testFile));
//            testFileBufferedWriter.append("test");
////            testFileBufferedWriter.close();
//        } catch (IOException ioe) {
//            Log.i(TAG, "Error uploading file: " + ioe.getMessage());
//        }
//
//        Amplify.Storage.uploadFile(
//                "testKey",
//                testFile,
//                success -> {
//                    Log.i(TAG, "Success in adding: " + success.getKey());
//                },
//                failure -> {
//                    Log.i(TAG, "Failed in adding: " + failure.getMessage(), failure);
//                }
//        );


        AuthUser currentUser = Amplify.Auth.getCurrentUser();

        if (currentUser == null)
        {
            Intent goToLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLoginActivityIntent);
        }

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        resources = getResources();

        CompletableFuture<List<Task>> taskCompletableFuture = new CompletableFuture<>();

        RecyclerView taskListRecyclerView = findViewById(R.id.taskListRecyclerView); //veritcal layout
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(lm);

        String teamName = sharedPref.getString(USER_TEAM_KEY, "");
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    List<Task> taskList = new ArrayList<>();
                    for (Task task : success.getData()) {
                        if (task.getTeam().getName().equals(teamName)) {
                            taskList.add(task);
                        }
                        Log.i(TAG, "Succeeded in adding to view: " + task.getTitle());
                    }
                    Collections.sort(taskList, (o1, o2) ->
                            -o1.getCreatedAt().compareTo(o2.getCreatedAt()));

//                    (o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                    taskCompletableFuture.complete(taskList);
//                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
                },
                failure -> {
                    Log.i(TAG, "failed");
                }
        );

        try {
            userTeamTasks = taskCompletableFuture.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(this, userTeamTasks);
        taskListRecyclerViewAdapter.notifyDataSetChanged();
        taskListRecyclerView.setAdapter(taskListRecyclerViewAdapter);


        Button addTaskButton = findViewById(R.id.homepageAddTaskButton);
        addTaskButton.setOnClickListener(view -> {
            Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(addTaskIntent);
        });

        Button allTasksButton = findViewById(R.id.homepageAllTasksButton);
        allTasksButton.setOnClickListener(view -> {
            Intent allTasksIntent = new Intent(MainActivity.this, AllTasksActivity.class);
            startActivity(allTasksIntent);
        });

        ImageView userSettingsImageView = findViewById(R.id.userSettingsImageView);
        userSettingsImageView.setOnClickListener(view -> {
            Intent userSettingsIntent = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(userSettingsIntent);
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener( onClick ->
                {
                    Amplify.Auth.signOut(
                            () -> {
                                Log.i(TAG, "Logout succeeded!");
                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Logout Successful!", Toast.LENGTH_SHORT).show());
                                },
                            failure -> {
                                Log.i(TAG, "Logout failed: " + failure.toString());
                                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Logout Unsuccessful!", Toast.LENGTH_SHORT).show());
                            }
                    );
                    Intent goToLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(goToLoginActivityIntent);
                }
        );
//        https://stackoverflow.com/a/5489320/16970042
//        if(currentUser != null){
//            loginButton.setVisibility(View.INVISIBLE);
//            signupButton.setVisibility(View.INVISIBLE);
//        } else {
//            logoutButton.setVisibility(View.INVISIBLE);
//        }

    }

//    protected void getTaskList(){
//
//    }

//    protected CompletableFuture<List<Task>> getListOfTasksFuture(){
//
//    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = sharedPref.getString(USER_USERNAME_KEY, "");
        String teamName = sharedPref.getString(USER_TEAM_KEY, "");
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    List<Task> taskList = new ArrayList<>();
                    for (Task task : success.getData()) {
                        if (task.getTeam().getName().equals(teamName)) {
                            taskList.add(task);
                        }
                        Log.i(TAG, "Succeeded in adding to view: " + task.getTitle());
                    }
                    Collections.sort(taskList, (o1, o2) ->
                            -o1.getCreatedAt().compareTo(o2.getCreatedAt()));
                    runOnUiThread(() -> {
                        taskListRecyclerViewAdapter.setTaskList(taskList);
                        taskListRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> {
                    Log.i(TAG, "failed");
                }
        );

        String userNickname = "";
        AuthUser currentUser = Amplify.Auth.getCurrentUser();
        if(currentUser != null) {
            //https://stackoverflow.com/a/10386080/16970042
            userNickname = currentUser.getUsername().substring(0, currentUser.getUsername().indexOf("@"));
        }

        if(!userNickname.equals("")){
            ((TextView) findViewById(R.id.homepageTitleTextView)).setText(resources.getString(R.string.UsernameTasks, userNickname));
        } else {
            ((TextView) findViewById(R.id.homepageTitleTextView)).setText(resources.getString(R.string.UsernameTasks, userName));
        }
        if (!teamName.equals("")) {
            ((TextView) findViewById(R.id.homepageTeamNameTextView)).setText(teamName);
        }
    }

}