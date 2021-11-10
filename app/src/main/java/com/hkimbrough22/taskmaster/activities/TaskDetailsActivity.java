package com.hkimbrough22.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.TaskNotFoundException;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class TaskDetailsActivity extends AppCompatActivity {

    public final static String TAG = "hkim_taskDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Intent intent = getIntent();

        CompletableFuture<Task> taskCompletableFuture = new CompletableFuture<>();

        String taskTitle = intent.getExtras().get(MainActivity.TASK_TITLE_EXTRA_STRING).toString();
        String taskBody = intent.getExtras().get(MainActivity.TASK_BODY_EXTRA_STRING).toString();
        String taskState = intent.getExtras().get(MainActivity.TASK_STATE_EXTRA_STRING).toString();

        Amplify.API.query(
            ModelQuery.list(Task.class),
            success ->
            {
                Task selectedTask = null;
                for (Task task : success.getData()) {
                    if(task.getTitle().equals(taskTitle)){
                        selectedTask = task;
                        break;
                    }
                }
                if(selectedTask == null){
                    throw new TaskNotFoundException();
                }
                    Log.i(TAG, "Successfully got Task: " + selectedTask.getTitle());
                    getAndSetImage(selectedTask.getTaskImageKey());

//                    Task selectedTask2 = selectedTask;  // TODO: investigate how to make this better
                runOnUiThread(() ->
                {
                    TextView taskTitleTextView = findViewById(R.id.taskDetailsTitleTextView);
                    taskTitleTextView.setText(taskTitle);
                    EditText taskBodyEditText = findViewById(R.id.taskDetailsDescriptionEditText);
                    taskBodyEditText.setText(taskBody);
                    TextView taskStateTextView = findViewById(R.id.taskStateTextView);
                    taskStateTextView.setText(taskState);

                });
                taskCompletableFuture.complete(selectedTask);
            },
            failure -> {
                Log.i(TAG, "Failed");
                taskCompletableFuture.complete(null);
            }
        );
    }

    protected void getAndSetImage(String s3ImageKey){
        if(s3ImageKey != null){
            if(!s3ImageKey.equals("")){
                Amplify.Storage.downloadFile(
                        s3ImageKey,
                        new File(getApplicationContext().getFilesDir(), s3ImageKey),
                        success -> {
                            Log.i(TAG, "Image file downloaded from S3 successfully with filename: " + success.getFile().getName());
                            runOnUiThread(() -> {
                                ImageView taskImageView = findViewById(R.id.taskImageView);
                                taskImageView.setImageBitmap(BitmapFactory.decodeFile(success.getFile().getPath()));
                            });
                        }, failure ->{
                            Log.e(TAG, "Image failed to download from S3. Key: " + s3ImageKey + " with error: " + failure.getMessage(), failure);
                        }
                );
            }
        }
    }
}