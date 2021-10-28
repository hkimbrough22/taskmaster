package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hkimbrough22.taskmaster.R;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        String taskTitle = intent.getExtras().get(MainActivity.TASK_TITLE_EXTRA_STRING).toString();
        String taskBody = intent.getExtras().get(MainActivity.TASK_BODY_EXTRA_STRING).toString();
        String taskState = intent.getExtras().get(MainActivity.TASK_STATE_EXTRA_STRING).toString();
        String taskAddedOn = intent.getExtras().get(MainActivity.TASK_ADDED_ON_EXTRA_STRING).toString();

        TextView taskTitleTextView = findViewById(R.id.taskDetailsTitleTextView);
        taskTitleTextView.setText(taskTitle);
        EditText taskBodyEditText = findViewById(R.id.taskDetailsDescriptionEditText);
        taskBodyEditText.setText(taskBody);
        TextView taskStateTextView = findViewById(R.id.taskStateTextView);
        taskStateTextView.setText(taskState);
        TextView taskAddedOnTextView = findViewById(R.id.taskAddedOnTextView);
        taskAddedOnTextView.setText(taskAddedOn);
    }
}