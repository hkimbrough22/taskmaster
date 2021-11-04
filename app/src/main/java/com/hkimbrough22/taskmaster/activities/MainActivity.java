package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.TAG;
import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.USER_TEAM_KEY;
import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.USER_USERNAME_KEY;

import static java.util.stream.Collectors.toList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.adapters.TaskListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public final static String SUBMITTED = "Submitted!";
    public final static String TASK_TITLE_EXTRA_STRING = "taskTitle";
    public final static String TASK_BODY_EXTRA_STRING = "taskBody";
    public final static String TASK_STATE_EXTRA_STRING = "taskState";
    public final static String TASK_ADDED_ON_EXTRA_STRING = "taskAddedOn";
    public final static String DATABASE_NAME = "hkim_taskmaster_db";
    public final static String TEAM_UNKNOWN_NAME = "Team Unknown";

    protected static SharedPreferences sharedPref;
    protected static Resources resources;

    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        if(task.getTeam().getName().equals(teamName)){
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

        List<Task> userTeamTasks = null;
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

    }

    @Override
    protected void onResume() {
        super.onResume();

        String userName = sharedPref.getString(USER_USERNAME_KEY, "");
        String teamName = sharedPref.getString(USER_TEAM_KEY, "");
        if (!userName.equals("")) {
            ((TextView) findViewById(R.id.homepageTitleTextView)).setText(resources.getString(R.string.UsernameTasks, userName));
        }
        if(!teamName.equals("")){
            ((TextView) findViewById(R.id.homepageTeamNameTextView)).setText(teamName);
        }

    }

    //TODO: Put the logic for getting the correct taskList  into a function and call in onCreate and onResume
}