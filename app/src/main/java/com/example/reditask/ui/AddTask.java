package com.example.reditask.ui;

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

public class AddTask extends AppCompatActivity {
    private static final String TAG = "AddTask";
    private static final String REQUIRED = "Required";
    public EditText date, time, title, desc;
    public Button submit;
    private DatabaseReference taskDB;
    private String status;

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
                        date.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
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

            switch (status) {
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
                case "create":
                    if (TextUtils.isEmpty(formTitle) || TextUtils.isEmpty(formDesc) || formDate == "Set Date" || formTime == "Set Time") {
                        Toast.makeText(this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        taskDB.child("Task").push()
                                .setValue(new Task(formTitle, formDesc, formDate, formTime))
                                .addOnSuccessListener(this, aVoid -> {
                                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    title.setText("");
                                    desc.setText("");
                                    date.setText("Set Date");
                                    time.setText("Set Time");
                                });
                    }
                    break;
            }


        });
    }

    private void checkUpdateOrCreateForm() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("key") != null) {
                getData();
                status = "edit";
            } else {
                status = "create";
            }
        }
    }

    private void getData() {
        if (getIntent().getExtras() != null) {
            final String getTitle = getIntent().getExtras().getString("title");
            final String getDesc = getIntent().getExtras().getString("desc");
            final String getDate = getIntent().getExtras().getString("date");
            final String getTime = getIntent().getExtras().getString("time");
            title.setText(getTitle);
            desc.setText(getDesc);
            date.setText(getDate);
            time.setText(getTime);
        }
    }

    private void updateTask(Task task) {
        String getKey = getIntent().getExtras().getString("key");
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
