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
import com.hkimbrough22.taskmaster.adapters.CartRecyclerViewAdapter;
import com.hkimbrough22.taskmaster.models.CartItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final static String SUBMITTED = "Submitted!";
    public final static String TASK_EXTRA_STRING = "taskName";

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

    //8. Add class CartItemViewHolder in adapter at bottom
    //9. change return to 20 or however much you need

    //10. set adapter for the view

    //11. give data










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView shoppingCartRecyclerView = findViewById(R.id.shoppingCartRecyclerView); //veritcal layout
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        shoppingCartRecyclerView.setLayoutManager(lm);
//        shoppingCarRecyclerView.setSoemthign
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("Test", new Date()));
        cartItemList.add(new CartItem("Test2", new Date()));
        cartItemList.add(new CartItem("Test3", new Date()));
        CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter(this, cartItemList); //"this" doesnt work at first, needs constructor with other info too
        shoppingCartRecyclerView.setAdapter(cartRecyclerViewAdapter);




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