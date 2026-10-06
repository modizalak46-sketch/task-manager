package com.example.taskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String description;
    public String date;
    public String time;
    public String priority;
    public boolean completed;

    public Task(String title, String description, String date,
                String time, String priority, boolean completed) {

        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.priority = priority;
        this.completed = completed;
    }
}