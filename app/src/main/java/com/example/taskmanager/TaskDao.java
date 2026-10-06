package com.example.taskmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE completed = 0")
    List<Task> getPendingTasks();

    @Query("SELECT * FROM tasks WHERE completed = 1")
    List<Task> getCompletedTasks();

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    void updateCompleted(int taskId, boolean completed);
}