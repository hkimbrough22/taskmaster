package com.hkimbrough22.taskmaster.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
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
    public final static int GET_FINE_LOCATION_PERMISSION = 1;

//    TaskListRecyclerViewAdapter taskListRecyclerViewAdapter;

    public ActivityResultLauncher<Intent> activityResultLauncher;
    FusedLocationProviderClient fusedLocationProviderClient;

    EditText taskTitle;
    EditText taskBody;
    EditText taskStatus;

    String taskLatitude = "";
    String taskLongitude = "";
    String city = "Unknown City";
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

            try {
                InputStream selectedImageInputStream = getContentResolver().openInputStream(selectedImageFileUri);
                uploadInputStreamToS3(selectedImageInputStream, selectedImageFilename);
                Log.i(TAG, "Successfully uploaded image to S3: " + selectedImageFilename);
            } catch (FileNotFoundException fnfe) {
                Log.e(TAG, "Could not find Input Stream: " + fnfe.getMessage(), fnfe);
            }
        });

        Button addImageButton = findViewById(R.id.addTaskAddImageButton);
        addImageButton.setOnClickListener(view -> {
            selectImageAndSaveToS3();

        });

        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GET_FINE_LOCATION_PERMISSION);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.flushLocations();
        // This will grab current location which is more up to date
        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken()
        {
            @Override
            public boolean isCancellationRequested()
            {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener)
            {
                return null;
            }
        }).addOnSuccessListener(location ->
        {
            Log.i(TAG, "Our latitude: " + location.getLatitude());
            taskLatitude = Double.toString(location.getLatitude());
            Log.i(TAG, "Our longitude: " + location.getLongitude());
            taskLongitude = Double.toString(location.getLongitude());

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
                    saveTaskToDB(success.getKey());

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
                    .taskLatitude(taskLatitude)
                    .taskLongitude(taskLongitude)
                    .city(city)
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