package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.USER_USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.adapters.TaskListRecyclerViewAdapter;
import com.hkimbrough22.taskmaster.models.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String SUBMITTED = "Submitted!";
    public final static String TASK_TITLE_EXTRA_STRING = "taskTitle";
    public final static String TASK_BODY_EXTRA_STRING = "taskBody";
    public final static String TASK_STATE_EXTRA_STRING = "taskState";
    public final static String TASK_ADDED_ON_EXTRA_STRING = "taskAddedOn";

    protected static SharedPreferences sharedPref;
    protected static Resources resources;

    //1. Add a RecyclerView to layout

    //2. Grab the RecyclerView by ID

    //3. Create and Set a linear layout manager for this RecyclerView

    //4. Create new package (models), and Data model, create constructor

    //4. Make a class whose sole purpose is to manage RecyclerViews (in new Adapters folder) should extends RecyclerView.Adapter
    //Instantiate adapter below, pass in data model

    //5. give recyclerviewadapter a constructor
    //6. Make fragment package and blank fragment, design (convert fragment to different layout (constraint))!!!!

    //7. Instatiate the fragment in Adapter

    //8. Add class TaskViewHolder in adapter at bottom
    //9. change return to 20 or however much you need

    //10. set adapter for the view

    //11. give data










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView taskListRecyclerView = findViewById(R.id.taskListRecyclerView); //veritcal layout
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        taskListRecyclerView.setLayoutManager(lm);
//        shoppingCarRecyclerView.setSoemthign
        List<Task> taskList = new ArrayList<>();
        taskList.add(new Task("Test", "testBody", "new", new Date()));
        taskList.add(new Task("Test2", "testBody2", "assigned", new Date()));
        taskList.add(new Task("Test3", "testBody3", "complete", new Date()));
        TaskListRecyclerViewAdapter taskListRecyclerViewAdapter = new TaskListRecyclerViewAdapter(this, taskList); //"this" doesnt work at first, needs constructor with other info too
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

//        ImageView task1 = findViewById(R.id.homepageImageView);
//        ImageView task2 = findViewById(R.id.homepageImageView2);
//        ImageView task3 = findViewById(R.id.homepageImageView3);
//
//        task1.setOnClickListener(view -> {
//            TextView taskNum = findViewById(R.id.homepageTask1TitleTextView);
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
//            taskDetailsIntent.putExtra(TASK_TITLE_EXTRA_STRING, taskNum.getText());
//            startActivity(taskDetailsIntent);
//        });
//
//        task2.setOnClickListener(view -> {
//            TextView taskNum = findViewById(R.id.homepageTask2TitleTextView);
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
//            taskDetailsIntent.putExtra(TASK_TITLE_EXTRA_STRING, taskNum.getText());
//            startActivity(taskDetailsIntent);
//        });
//
//        task3.setOnClickListener(view -> {
//            TextView taskNum = findViewById(R.id.homepageTask3TitleTextView);
//            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
//            taskDetailsIntent.putExtra(TASK_TITLE_EXTRA_STRING, taskNum.getText());
//            startActivity(taskDetailsIntent);
//        });

    }

    @Override
    protected void onResume(){
        super.onResume();

        String userName = sharedPref.getString(USER_USERNAME_KEY, "");
        if(!userName.equals("")){
            ((TextView) findViewById(R.id.homepageTitleTextView)).setText(resources.getString(R.string.UsernameTasks, userName));
        }

    }
}