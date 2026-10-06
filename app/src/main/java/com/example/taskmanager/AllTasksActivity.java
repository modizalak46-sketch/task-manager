package com.example.taskmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AllTasksActivity extends AppCompatActivity {

    LinearLayout taskContainer;

    Button btnAll;
    Button btnPending;
    Button btnCompleted;
    Button btnAddTask;
    Button btnBack;

    TaskDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        // Views Find કરો
        btnBack = findViewById(R.id.btnBack);
        taskContainer = findViewById(R.id.taskContainer);
        btnAll = findViewById(R.id.btnAll);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);
        btnAddTask = findViewById(R.id.btnAddTask);

        // Database
        database = TaskDatabase.getInstance(this);

        // Null Check ઉમેર્યા છે જેથી ID ના મળે તો ક્રેશ ન થાય
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(AllTasksActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (btnAll != null) {
            btnAll.setOnClickListener(v -> loadTasks("all"));
        }

        if (btnPending != null) {
            btnPending.setOnClickListener(v -> loadTasks("pending"));
        }

        if (btnCompleted != null) {
            btnCompleted.setOnClickListener(v -> loadTasks("completed"));
        }

        if (btnAddTask != null) {
            btnAddTask.setOnClickListener(v -> {
                Intent intent = new Intent(AllTasksActivity.this, AddTaskActivity.class);
                startActivity(intent);
            });
        }

        // Load tasks
        loadTasks("all");
    }

    private void loadTasks(String type) {
        if (taskContainer == null) {
            Toast.makeText(this, "XML માં taskContainer ID મળતું નથી!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear old tasks
        taskContainer.removeAllViews();

        List<Task> tasks = null;

        if (database != null && database.taskDao() != null) {
            // Filter tasks
            if ("pending".equals(type)) {
                tasks = database.taskDao().getPendingTasks();
            } else if ("completed".equals(type)) {
                tasks = database.taskDao().getCompletedTasks();
            } else {
                tasks = database.taskDao().getAllTasks();
            }
        }

        if (tasks == null) return;

        // Display tasks
        for (Task task : tasks) {

            LinearLayout taskLayout = new LinearLayout(this);
            taskLayout.setOrientation(LinearLayout.VERTICAL);
            taskLayout.setPadding(18, 18, 18, 18);
            taskLayout.setBackgroundColor(Color.WHITE);

            // Title
            TextView title = new TextView(this);
            title.setText(task.title != null ? task.title : "");
            title.setTextColor(Color.BLACK);
            title.setTextSize(18);
            title.setTypeface(null, 1);

            // Details
            TextView details = new TextView(this);
            details.setText(
                    "Date: " + (task.date != null ? task.date : "") +
                            "\nTime: " + (task.time != null ? task.time : "") +
                            "\nPriority: " + (task.priority != null ? task.priority : "")
            );
            details.setTextColor(Color.GRAY);
            details.setTextSize(14);

            // Checkbox
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(task.completed ? "Completed" : "Pending");
            checkBox.setChecked(task.completed);

            // Checkbox click
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (database != null && database.taskDao() != null) {
                    database.taskDao().updateCompleted(task.id, isChecked);
                }
                checkBox.setText(isChecked ? "Completed" : "Pending");
            });

            // Add views
            taskLayout.addView(title);
            taskLayout.addView(details);
            taskLayout.addView(checkBox);

            // Add task to screen
            taskContainer.addView(taskLayout);
        }
    }
}