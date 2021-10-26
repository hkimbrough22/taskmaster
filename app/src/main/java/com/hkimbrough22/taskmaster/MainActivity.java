package com.hkimbrough22.taskmaster;

import static com.hkimbrough22.taskmaster.UserSettingsActivity.USER_USERNAME_KEY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public final static String SUBMITTED = "Submitted!";
    public final static String TASK_EXTRA_STRING = "taskNum";

    protected static SharedPreferences sharedPref;
    protected static Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        ImageView task1 = findViewById(R.id.homepageImageView);
        ImageView task2 = findViewById(R.id.homepageImageView2);
        ImageView task3 = findViewById(R.id.homepageImageView3);

        task1.setOnClickListener(view -> {
            TextView taskNum = findViewById(R.id.homepageTask1TitleTextView);
            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            taskDetailsIntent.putExtra(TASK_EXTRA_STRING, taskNum.getText());
            startActivity(taskDetailsIntent);
        });

        task2.setOnClickListener(view -> {
            TextView taskNum = findViewById(R.id.homepageTask2TitleTextView);
            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            taskDetailsIntent.putExtra(TASK_EXTRA_STRING, taskNum.getText());
            startActivity(taskDetailsIntent);
        });

        task3.setOnClickListener(view -> {
            TextView taskNum = findViewById(R.id.homepageTask3TitleTextView);
            Intent taskDetailsIntent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            taskDetailsIntent.putExtra(TASK_EXTRA_STRING, taskNum.getText());
            startActivity(taskDetailsIntent);
        });

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