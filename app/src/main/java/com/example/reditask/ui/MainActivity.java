package com.example.reditask.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reditask.R;
import com.example.reditask.adapter.TaskAdapter;
import com.example.reditask.model.Task;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TaskAdapter taskAdapter;
    private RecyclerView rvTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        rvTask = findViewById(R.id.rv_tasks);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference("Task");

        displayListTask();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> toaddtask());
    }

    private void displayListTask() {
        rvTask.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTask.setLayoutManager(linearLayoutManager);


        FirebaseRecyclerOptions<Task> taskFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Task>()
                .setQuery(mDatabase, Task.class)
                .build();
        taskAdapter = new TaskAdapter(taskFirebaseRecyclerOptions);
        taskAdapter.notifyDataSetChanged();
        rvTask.setAdapter(taskAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (taskAdapter != null) {
            taskAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (taskAdapter != null) {
            taskAdapter.stopListening();
        }
    }

    public void toaddtask() {
        Intent i = new Intent(MainActivity.this, AddTask.class);
        startActivity(i);
    }

}
