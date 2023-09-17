package com.example.school.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.school.Logic.Task;
import com.example.school.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public final Activity activity;
    ArrayList<Task> list;

    public TaskAdapter(ArrayList<Task> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_of_tasks, parent, false);

        view.setLongClickable(true);
        return new TaskAdapter.TaskViewHolder(view);
    }

    Dialog dialog;


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;

        public TaskViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_of_task);
            comment = itemView.findViewById(R.id.comment_of_task);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = list.get(position);

        holder.name.setText(task.getName());
        holder.comment.setText(task.getComment());


        holder.itemView.setOnClickListener(click -> {

        });
    }

}