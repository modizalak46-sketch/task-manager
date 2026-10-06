package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnAddTask;
    Button btnAllTasks;

    LinearLayout taskContainer;

    TaskDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Find Views
        btnAddTask = findViewById(R.id.btnAddTask);
        btnAllTasks = findViewById(R.id.btnAllTasks);
        taskContainer = findViewById(R.id.taskContainer);

        // Database
        database = TaskDatabase.getInstance(this);

        // Add New Task
        btnAddTask.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddTaskActivity.class
            );

            startActivity(intent);
        });

        // View All Tasks
        btnAllTasks.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AllTasksActivity.class
            );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (database != null) {
            loadTasks();
        }
    }

    private void loadTasks() {

        taskContainer.removeAllViews();

        List<Task> tasks = database.taskDao().getAllTasks();

        for (Task task : tasks) {

            LinearLayout taskLayout = new LinearLayout(this);

            taskLayout.setOrientation(LinearLayout.HORIZONTAL);
            taskLayout.setPadding(12, 12, 12, 12);

            // Checkbox
            CheckBox checkBox = new CheckBox(this);

            checkBox.setChecked(task.completed);

            // Text Layout
            LinearLayout textLayout = new LinearLayout(this);

            textLayout.setOrientation(LinearLayout.VERTICAL);

            // Task Title
            TextView title = new TextView(this);

            title.setText(task.title);
            title.setTextSize(16);
            title.setTextColor(Color.BLACK);
            title.setTypeface(null, 1);

            // Task Details
            TextView details = new TextView(this);

            details.setText(
                    task.date + " • " +
                            task.time + " • " +
                            task.priority
            );

            details.setTextSize(13);
            details.setTextColor(Color.GRAY);

            // Add text views
            textLayout.addView(title);
            textLayout.addView(details);

            // Add checkbox and text
            taskLayout.addView(checkBox);
            taskLayout.addView(textLayout);

            // Add task to container
            taskContainer.addView(taskLayout);

            // Checkbox click
            checkBox.setOnCheckedChangeListener(
                    (buttonView, isChecked) -> {

                        database.taskDao().updateCompleted(
                                task.id,
                                isChecked
                        );
                    }
            );
        }
    }
}