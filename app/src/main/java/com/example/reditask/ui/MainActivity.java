package com.example.reditask.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reditask.R;
import com.example.reditask.adapter.TaskAdapter;
import com.example.reditask.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth mAuth;
    private TextView logout;
    private String userId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        logout = findViewById(R.id.tv_logout);
        rvTask = findViewById(R.id.rv_tasks);
        progressBar = findViewById(R.id.progress);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> toaddtask());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Anda yakin ingin keluar?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        mAuth.signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            userId = mAuth.getCurrentUser().getUid();
            displayListTask();
        }
    }

    private void displayListTask() {
        progressBar.setVisibility(View.VISIBLE);
        mDatabase.child("Task").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listTask = new ArrayList<>();
                progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.GONE);

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
                        if (mDatabase != null) {
                            mDatabase.child("Task").child(userId)
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
