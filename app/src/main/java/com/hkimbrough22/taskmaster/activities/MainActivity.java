package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.TAG;
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
import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.adapters.TaskListRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public final static String SUBMITTED = "Submitted!";
    public final static String TASK_TITLE_EXTRA_STRING = "taskTitle";
    public final static String TASK_BODY_EXTRA_STRING = "taskBody";
    public final static String TASK_STATE_EXTRA_STRING = "taskState";
    public final static String TASK_ADDED_ON_EXTRA_STRING = "taskAddedOn";
    public final static String DATABASE_NAME = "hkim_taskmaster_db";

    protected static SharedPreferences sharedPref;
    protected static Resources resources;

    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView taskListRecyclerView = findViewById(R.id.taskListRecyclerView); //veritcal layout
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(lm);
        Amplify.API.query(
                ModelQuery.list(Task.class),
                success -> {
                    List<Task> taskList = new ArrayList<>();
                    for (Task task : success.getData()) {
                        taskList.add(task);
                        Log.i(TAG, "Succeeded in adding to view: " + task.getTitle());
                    }
//                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
                    runOnUiThread(() -> {
                        taskListRecyclerViewAdapter.setTaskList(taskList);
                        taskListRecyclerViewAdapter.notifyDataSetChanged();
                    });
                },
                failure -> {
                    Log.i(TAG, "failed");
                }
        );

        List<Task> taskList = new ArrayList<>();
        taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(this, taskList);
        taskListRecyclerView.setAdapter(taskListRecyclerViewAdapter);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        resources = getResources();

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
        if (!userName.equals("")) {
            ((TextView) findViewById(R.id.homepageTitleTextView)).setText(resources.getString(R.string.UsernameTasks, userName));
        }

    }
}