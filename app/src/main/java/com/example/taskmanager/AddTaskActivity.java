package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    EditText etTaskTitle, etDescription;
    Button btnDate, btnTime, btnSaveTask;
    Spinner spinnerPriority;

    TaskDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTaskTitle = findViewById(R.id.etTaskTitle);
        etDescription = findViewById(R.id.etDescription);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        spinnerPriority = findViewById(R.id.spinnerPriority);

        database = TaskDatabase.getInstance(this);

        String[] priorities = {"Low", "Medium", "High"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                priorities
        );

        spinnerPriority.setAdapter(adapter);

        // Date
        btnDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddTaskActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String date = selectedDay + "/" +
                                (selectedMonth + 1) + "/" +
                                selectedYear;

                        btnDate.setText(date);
                    },
                    year, month, day
            );

            dialog.show();
        });

        // Time
        btnTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog dialog = new TimePickerDialog(
                    AddTaskActivity.this,
                    (view, selectedHour, selectedMinute) -> {

                        String time = String.format(
                                "%02d:%02d",
                                selectedHour,
                                selectedMinute
                        );

                        btnTime.setText(time);
                    },
                    hour, minute, false
            );

            dialog.show();
        });

        // Save Task
        btnSaveTask.setOnClickListener(v -> {

            String title = etTaskTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String date = btnDate.getText().toString();
            String time = btnTime.getText().toString();
            String priority = spinnerPriority.getSelectedItem().toString();

            if (title.isEmpty()) {
                etTaskTitle.setError("Please enter task title");
                return;
            }

            Task task = new Task(
                    title,
                    description,
                    date,
                    time,
                    priority,
                    false
            );

            database.taskDao().insert(task);

            Toast.makeText(
                    AddTaskActivity.this,
                    "Task Saved Successfully!",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}