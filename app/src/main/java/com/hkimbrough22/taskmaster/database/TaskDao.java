package com.hkimbrough22.taskmaster.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hkimbrough22.taskmaster.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    public void insert(Task task);



    @Query("SELECT * FROM Task")
    public List<Task> findAll();
}
