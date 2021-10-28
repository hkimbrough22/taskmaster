package com.hkimbrough22.taskmaster.adapters;

import static com.hkimbrough22.taskmaster.activities.MainActivity.TASK_ADDED_ON_EXTRA_STRING;
import static com.hkimbrough22.taskmaster.activities.MainActivity.TASK_BODY_EXTRA_STRING;
import static com.hkimbrough22.taskmaster.activities.MainActivity.TASK_STATE_EXTRA_STRING;
import static com.hkimbrough22.taskmaster.activities.MainActivity.TASK_TITLE_EXTRA_STRING;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hkimbrough22.taskmaster.R;
import com.hkimbrough22.taskmaster.activities.TaskDetailsActivity;
import com.hkimbrough22.taskmaster.models.Task;

import java.util.List;

//Step 4.
public class TaskListRecyclerViewAdapter extends RecyclerView.Adapter<TaskListRecyclerViewAdapter.TaskViewHolder> {

    AppCompatActivity associatedActivity;
    List<Task> taskList;


    public TaskListRecyclerViewAdapter(AppCompatActivity associatedActivity, List<Task> taskList){
        this.associatedActivity = associatedActivity;
        this.taskList = taskList;
    }

    //Step 7 here
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task, parent,  false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(fragment);

       return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
//        Change the data when the viewholder/fragments cycle
        Task task = taskList.get(position);
        View taskFragment = holder.itemView;
        TextView taskFragmentTextView = taskFragment.findViewById(R.id.taskFragmentTextView);
        taskFragmentTextView.setText(task.toString());

        taskFragment.setOnClickListener(view -> {
            Intent taskDetailsIntent = new Intent(associatedActivity, TaskDetailsActivity.class);
            taskDetailsIntent.putExtra(TASK_TITLE_EXTRA_STRING, task.getTitle());
            taskDetailsIntent.putExtra(TASK_BODY_EXTRA_STRING, task.getBody());
            taskDetailsIntent.putExtra(TASK_STATE_EXTRA_STRING, task.getState());
            taskDetailsIntent.putExtra(TASK_ADDED_ON_EXTRA_STRING, task.getAddedOn());
            associatedActivity.startActivity(taskDetailsIntent);
        });
    }


    @Override
    public int getItemCount() {
        return taskList.size();
    }

    //Step 8 here
    //Step 11 here too
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;

        //TODO: add some data variables later
        public TaskViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
