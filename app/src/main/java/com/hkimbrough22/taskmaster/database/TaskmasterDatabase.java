package com.hkimbrough22.taskmaster.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hkimbrough22.taskmaster.models.Task;

import java.util.Arrays;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
@TypeConverters({TaskmasterConverters.class})
public abstract class TaskmasterDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
}
