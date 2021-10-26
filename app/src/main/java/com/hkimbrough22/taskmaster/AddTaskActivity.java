package com.hkimbrough22.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = (Button) findViewById(R.id.addTaskSubmitButton);
        addTaskButton.setOnClickListener(view -> {
            TextView notificationTextView = findViewById(R.id.addTaskNotificationTextView);
            notificationTextView.setText(MainActivity.SUBMITTED);
        });
    }
}