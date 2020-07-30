package com.example.reditask.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reditask.R;
import com.example.reditask.adapter.TaskAdapter;
import com.example.reditask.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TaskAdapter taskAdapter;
    private RecyclerView rvTask;
    private ArrayList<Task> listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        rvTask = findViewById(R.id.rv_tasks);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        displayListTask();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> toaddtask());

    }

    private void displayListTask() {
        mDatabase.child("Task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTask = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Task task = dataSnapshot.getValue(Task.class);

                    if (task != null) {
                        task.setKey(dataSnapshot.getKey());
                    }
                    listTask.add(task);
                }

                rvTask.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                rvTask.setLayoutManager(linearLayoutManager);

                taskAdapter = new TaskAdapter(listTask);
                taskAdapter.notifyDataSetChanged();
                rvTask.setAdapter(taskAdapter);

                taskAdapter.setOnListTaskClick(new TaskAdapter.OnListClickListener() {
                    @Override
                    public void onListClickListener(ArrayList<Task> list, int postion) {
                        Intent intent = new Intent(MainActivity.this, AddTask.class);
                        intent.putExtra("title", list.get(postion).getTask_title());
                        intent.putExtra("desc", list.get(postion).getTask_desc());
                        intent.putExtra("date", list.get(postion).getTask_date());
                        intent.putExtra("time", list.get(postion).getTask_time());
                        intent.putExtra("key", list.get(postion).getKey());
                        intent.putExtra("status", "edit");
                        startActivity(intent);
                    }
                });

                taskAdapter.setDeleteClickListener(new TaskAdapter.DeleteClickListener() {
                    @Override
                    public void deleteItem(String key) {
                        if (mDatabase != null){
                            mDatabase.child("Task")
                                    .child(key)
                                    .removeValue()
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(MainActivity.this, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void toaddtask() {
        Intent i = new Intent(MainActivity.this, AddTask.class);
        i.putExtra("status", "create");
        startActivity(i);
    }

}
