package com.hkimbrough22.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;
import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.adapters.TaskListRecyclerViewAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AddTaskActivity extends AppCompatActivity {

    public final static String TAG = "hkim_addTaskActivity";
    public final static String NEW_TASK = "newTask";

    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;

    public ActivityResultLauncher<Intent> activityResultLauncher;

    EditText taskTitle;
    EditText taskBody;
    EditText taskStatus;

    String taskStatusLower;
    String awsImageKey;

    Team selectedTeam = null;

    Uri selectedImageFileUri;
    String selectedImageFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        activityResultLauncher = getImagePickingActivityResultLauncher();

        Intent intent = getIntent();

        if((intent.getType() != null) && (intent.getType().startsWith("image/")))
        {
            Uri incomingFileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (incomingFileUri != null)
            {
                try
                {
                    selectedImageFileUri = incomingFileUri;
                    selectedImageFilename = getFilenameFromURI(incomingFileUri);

                    InputStream incomingImageFileInputStream = getContentResolver().openInputStream(incomingFileUri);
                    ImageView previewImageMainActivityImageView = findViewById(R.id.addTaskPreviewImageView);
                    previewImageMainActivityImageView.setImageBitmap(BitmapFactory.decodeStream(incomingImageFileInputStream));
                }
                catch (FileNotFoundException fnfe)
                {
                    Log.e(TAG, "Could not get file from file picker! " + fnfe.getMessage(), fnfe);
                }
            }
        }

        //Sets up spinners
        Spinner teamSpinner = findViewById(R.id.addTaskTeamSpinner);

        CompletableFuture<List<Team>> teamCompletableFuture = new CompletableFuture<>();
        //finding teams in DB for spinner selections
        List<Team> teams = new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(Team.class),
                success -> {
                    if (success.hasData()) {
                        for (Team team : success.getData()) {
                            teams.add(team);
                        }
                        teamCompletableFuture.complete(teams);
                    }
                },
                failure -> {
                    Log.i(TAG, "failed");
                    teamCompletableFuture.complete(null);
                }
        );

        List<Team> teams1 = null;
        try {
            teams1 = teamCompletableFuture.get();
        } catch (InterruptedException ie) {
            Log.i(TAG, "InterruptedException while getting team:" + ie.getMessage());
        } catch (ExecutionException ee) {
            Log.i(TAG, "ExecutionException while getting team:" + ee.getMessage());
        }

        List<String> teamNames = new ArrayList<>();
        for(Team team : teams1){
            if(!teamNames.contains(team.getName())){
            teamNames.add(team.getName());
            }
        }
        teamNames.sort(Comparator.naturalOrder());
        teamSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames));
        List<Team> teams2 = teams1;

        Button addTaskButton = findViewById(R.id.addTaskSubmitButton);
        addTaskButton.setOnClickListener(view -> {

            String selection = teamSpinner.getSelectedItem().toString();
            for(Team team : teams2){
                if(selection.equals(team.getName())){
                    selectedTeam = team;
                }
                teamNames.add(team.getName());
            }

            taskTitle = findViewById(R.id.addTaskTitleEditText);
            taskBody = findViewById(R.id.addTaskDescriptionEditText);
            taskStatus = findViewById(R.id.addTaskStateEditText);
            taskStatusLower = taskStatus.getText().toString().toLowerCase();

            if(awsImageKey != null){
            saveTaskToDB(awsImageKey);
            } else {
                saveTaskToDB("");
            }

            //selectFileAndSaveToS3(businessUnit, productNamePlainTextString); //*******************************************************

            // take teamString and do query to DB for team
            // take team and use that for taskbuilder below
//            Amplify.API.query(
//                    ModelQuery.list(Team.class),
//                    success -> {
//                        if (success.hasData()) {
//                            for (Team team : success.getData()) {
//                                if (team.getName().equals(selection)) {
//                                    selectedTeam = team;
//                                }
//                            }
//                        }
////                    taskList = taskList.stream().map(Task::getCreatedAt).sorted().collect(toList());
//                    },
//                    failure -> {
//                        Log.i(TAG, "failed");
//                    }
//            );


        });

        Button addImageButton = findViewById(R.id.addTaskAddImageButton);
        addImageButton.setOnClickListener(view -> {
            selectImageAndSaveToS3();

        });
    }

    protected void selectImageAndSaveToS3(){
        Intent imagePickingIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imagePickingIntent.setType("*/*");
        imagePickingIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg"});
//        startActivity(imagePickingIntent);
//        ActivityResultLauncher<Intent> activityResultLauncher = getImagePickingActivityResultLauncher();
        activityResultLauncher.launch(imagePickingIntent);
    }

    protected ActivityResultLauncher<Intent> getImagePickingActivityResultLauncher(){
        ActivityResultLauncher<Intent> imagePickingActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            if(result.getData() != null){
                                Uri pickedImageFileUri = result.getData().getData();
                                try {
                                    InputStream pickedImageInputStream = getContentResolver().openInputStream(pickedImageFileUri);
                                    String pickedImageFilename = getFilenameFromURI(pickedImageFileUri);
                                    Log.i(TAG, "Succeeded in getting input stream from file on phone: " + pickedImageFilename);
                                    uploadInputStreamToS3(pickedImageInputStream, pickedImageFilename);
                                } catch (FileNotFoundException fnfe) {
                                    Log.e(TAG, "Could not get file from filepicker: " + fnfe.getMessage(), fnfe);
                                }
                            }
                        }
                    }
                }
        );
                return imagePickingActivityResultLauncher;
    }

    protected void uploadInputStreamToS3(InputStream pickImageFileInputStream, String pickImageFilename){ //make sure to call this
        Amplify.Storage.uploadInputStream(
                pickImageFilename,
                pickImageFileInputStream,
                success -> {
                    Log.i(TAG, "Succeeded in uploading file to S3. Key is: " + success.getKey());
//                    saveTaskToDB(success.getKey());
                    awsImageKey = success.getKey();
                },

                failure -> {
                    Log.e(TAG, "Failed to upload file to S3: " + pickImageFilename + " with error: " + failure.getMessage(), failure);
                }
        );
    }


    protected void saveTaskToDB(String awsImageKey)
    {
        if (!taskStatusLower.equals("new") &&
                !taskStatusLower.equals("in progress") &&
                !taskStatusLower.equals("complete") &&
                !taskStatusLower.equals("unknown")) {
            Toast.makeText(this, R.string.invalidStatus, Toast.LENGTH_LONG).show();
        } else {
            Task newTask = Task.builder()
                    .title(taskTitle.getText().toString())
                    .team(selectedTeam)
                    .body(taskBody.getText().toString())
                    .state(taskStatus.getText().toString())
                    .taskImageKey(awsImageKey)
                    .build();
            Amplify.API.mutate(
                    ModelMutation.create(newTask),
                    success -> Log.i(TAG, "Successfully added " + newTask.getTitle()),
                    failure -> Log.e(TAG, "Failed to create new Task because " + failure.getMessage(), failure)
            );

            Toast.makeText(this, R.string.taskAdded, Toast.LENGTH_LONG).show();
            Intent mainActivityIntent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

//https://stackoverflow.com/a/25005243/16970042
    @SuppressLint("Range")
    public String getFilenameFromURI(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public int getSpinnerIndex(Spinner spinner, String selection) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(selection)) {
                return i;
            }
        }
        return 0;
    }
}