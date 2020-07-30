package com.example.reditask.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reditask.R;
import com.example.reditask.model.Task;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private ArrayList<Task> listTask;
    private OnListClickListener onListClickListener;
    private DeleteClickListener deleteClickListener;

    public TaskAdapter(ArrayList<Task> listTask) {
        this.listTask = listTask;
    }

    public void setOnListTaskClick(OnListClickListener onListClickListener) {
        this.onListClickListener = onListClickListener;
    }

    public void setDeleteClickListener(DeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tasks,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.title.setText(listTask.get(position).getTask_title());
        holder.desc.setText(listTask.get(position).getTask_desc());
        holder.date.setText(listTask.get(position).getTask_date());
        holder.itemView.setOnClickListener(v -> onListClickListener.onListClickListener(listTask, position));
        holder.delete.setOnClickListener(v -> deleteClickListener.deleteItem(listTask.get(position).getKey()));
    }

    @Override
    public int getItemCount() {
        return listTask.size();
    }

    public interface DeleteClickListener {
        void deleteItem(String key);
    }

    public interface OnListClickListener {
        void onListClickListener(ArrayList<Task> list, int postion);
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc, date;
        Button delete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_task);
            desc = itemView.findViewById(R.id.desc_task);
            date = itemView.findViewById(R.id.date_task);
            delete = itemView.findViewById(R.id.hapus_button);
        }
    }

}
