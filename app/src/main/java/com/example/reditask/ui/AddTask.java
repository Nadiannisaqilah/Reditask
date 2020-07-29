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
    public EditText date, time, title, desc, newDate, newTime, newTitle, newDesc;
    public Button submit, update;
    private DatabaseReference taskDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //[START Initialize DB]
        taskDB = FirebaseDatabase.getInstance().getReference();
        getData();
        //[END Initialize DB]

        Toolbar toolbar = findViewById(R.id.addtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        date = findViewById(R.id.et_date);
        time = findViewById(R.id.et_time);
        title = findViewById(R.id.et_taskname);
        desc = findViewById(R.id.et_taskdesc);
        submit = findViewById(R.id.bt_submit);

        newDate = findViewById(R.id.et_date);
        newTime = findViewById(R.id.et_time);
        newTitle = findViewById(R.id.et_taskname);
        newDesc = findViewById(R.id.et_taskdesc);
        update = findViewById(R.id.bt_submit);

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
        });

        update.setOnClickListener(view -> {
            String formTitle = newTitle.getText().toString();
            String formDesc = newDesc.getText().toString();
            String formDate = newDate.getText().toString();
            String formTime = newTime.getText().toString();

            if (TextUtils.isEmpty(formTitle) || TextUtils.isEmpty(formDesc) || formDate == "Set Date" || formTime == "Set Time") {
                Toast.makeText(this, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show();
            } else {
                Task setTask = new Task();
                setTask.setTask_title(newTitle.getText().toString());
                setTask.setTask_desc(newDesc.getText().toString());
                setTask.setTask_date(newDate.getText().toString());
                setTask.setTask_time(newTime.getText().toString());
                updateTask(setTask);
            }
        });
    }

    private void getData() {
        final String getTitle = getIntent().getExtras().getString("title");
        final String getDesc = getIntent().getExtras().getString("desc");
        final String getDate = getIntent().getExtras().getString("date");
        final String getTime = getIntent().getExtras().getString("time");

        newTitle.setText(getTitle);
        newDesc.setText(getDesc);
        newDate.setText(getDate);
        newTime.setText(getTime);
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
