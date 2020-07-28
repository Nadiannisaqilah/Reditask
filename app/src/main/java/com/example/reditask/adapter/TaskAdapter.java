package com.example.reditask.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reditask.R;
import com.example.reditask.model.Task;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TaskAdapter extends FirebaseRecyclerAdapter<Task, TaskAdapter.TaskViewHolder> {

    public TaskAdapter(@NonNull FirebaseRecyclerOptions<Task> options) {
        super(options);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks,
                parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i, @NonNull Task task) {
        taskViewHolder.title.setText(task.getTask_title());
        taskViewHolder.desc.setText(task.getTask_desc());
        taskViewHolder.date.setText(task.getTask_date());
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc, date;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_task);
            desc = itemView.findViewById(R.id.desc_task);
            date = itemView.findViewById(R.id.date_task);
        }
    }
}
