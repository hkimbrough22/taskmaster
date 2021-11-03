package com.hkimbrough22.taskmaster.activities;

import static com.hkimbrough22.taskmaster.activities.UserSettingsActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.hkimbrough22.taskmaster.R;

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
            Task newTask = Task.builder()
                    .title(taskTitle.getText().toString())
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
        });
    }
}