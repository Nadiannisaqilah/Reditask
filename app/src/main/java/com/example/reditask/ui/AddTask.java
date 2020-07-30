package com.example.reditask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.reditask.R;
import com.example.reditask.model.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Objects;

public class AddTask extends AppCompatActivity {
    public EditText date, time, title, desc;
    public Button submit;
    private DatabaseReference taskDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //[START Initialize DB]
        taskDB = FirebaseDatabase.getInstance().getReference();
        //[END Initialize DB]

        Toolbar toolbar = findViewById(R.id.addtoolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        date = findViewById(R.id.et_date);
        time = findViewById(R.id.et_time);
        title = findViewById(R.id.et_taskname);
        desc = findViewById(R.id.et_taskdesc);
        submit = findViewById(R.id.bt_submit);

        // Check Update or Create Form
        checkUpdateOrCreateForm();
        // Listener
        listener();
    }

    private void listener() {
        date.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        int month = monthOfYear + 1;
                        date.setText(year + "-" + month + "-" + dayOfMonth);
                    },
                    now.get(Calendar.YEAR), // Initial year selection
                    now.get(Calendar.MONTH), // Initial month selection
                    now.get(Calendar.DAY_OF_MONTH) // Inital day selection
            );

            dpd.show(getSupportFragmentManager(), "Datepickerdialog");
        });

        time.setOnClickListener(view -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    (view1, hourOfDay, minute, second) -> {
                        time.setText(hourOfDay + ":" + minute);
                    },
                    now.get(Calendar.HOUR),
                    now.get(Calendar.MINUTE),
                    now.get(Calendar.SECOND),
                    true
            );
            tpd.show(getSupportFragmentManager(), "Timepickerdialog");
        });

        submit.setOnClickListener(view -> {
            String formTitle = title.getText().toString();
            String formDesc = desc.getText().toString();
            String formDate = date.getText().toString();
            String formTime = time.getText().toString();

            switch (getIntent().getStringExtra("status")) {
                case "create":
                    if (TextUtils.isEmpty(formTitle) || TextUtils.isEmpty(formDesc) || formDate == "Set Date" || formTime == "Set Time") {
                        Toast.makeText(this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        taskDB.child("Task").push()
                                .setValue(new Task(formTitle, formDesc, formDate, formTime))
                                .addOnSuccessListener(this, aVoid -> {
                                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AddTask.this, MainActivity.class);
                                    startActivity(i);
                                });
                    }
                    break;
                case "edit":
                    if (TextUtils.isEmpty(formTitle) || TextUtils.isEmpty(formDesc) || formDate == "Set Date" || formTime == "Set Time") {
                        Toast.makeText(this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        Task setTask = new Task();
                        setTask.setTask_title(title.getText().toString());
                        setTask.setTask_desc(desc.getText().toString());
                        setTask.setTask_date(date.getText().toString());
                        setTask.setTask_time(time.getText().toString());
                        updateTask(setTask);
                    }
                    break;
            }

        });
    }

    private void checkUpdateOrCreateForm() {
        if (Objects.equals(Objects.requireNonNull(getIntent().getExtras()).getString("status"), "edit")) {
            getData();
        }
    }

    private void getData() {
        if (getIntent().getExtras() != null) {
            final String getTitle = getIntent().getStringExtra("title");
            final String getDesc = getIntent().getStringExtra("desc");
            final String getDate = getIntent().getStringExtra("date");
            final String getTime = getIntent().getStringExtra("time");
            title.setText(getTitle);
            desc.setText(getDesc);
            date.setText(getDate);
            time.setText(getTime);
        }
    }

    private void updateTask(Task task) {
        String getKey = getIntent().getStringExtra("key");
        taskDB.child("Task").child(getKey)
                .setValue(task)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                    title.setText("");
                    desc.setText("");
                    date.setText("Set Date");
                    time.setText("Set Time");
                    finish();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
