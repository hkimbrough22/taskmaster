package com.hkimbrough22.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();
        String task = intent.getExtras().get(MainActivity.TASK_EXTRA_STRING).toString();

        TextView taskTitle = findViewById(R.id.taskDetailsTitleTextView);
        taskTitle.setText(task);

    }
}