package com.example.reditask;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        //[START Initialize DB]
        taskDB = FirebaseDatabase.getInstance().getReference();
        //[END Initialize DB]

        date = findViewById(R.id.et_date);
        time = findViewById(R.id.et_time);
        title = findViewById(R.id.et_taskname);
        desc = findViewById(R.id.et_taskdesc);
        submit = findViewById(R.id.bt_submit);

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

            if (formTitle.length() == 0 || formDesc.length() == 0 || formDate.length() == 0 || formTime.length() == 0) {
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
    }
}
