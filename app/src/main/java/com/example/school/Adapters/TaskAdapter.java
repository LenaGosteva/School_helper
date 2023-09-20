package com.example.school.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.App;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.TaskActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

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
        public CheckBox isCompleted;

        public TaskViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_of_task);
            comment = itemView.findViewById(R.id.comment_of_task);
            isCompleted = itemView.findViewById(R.id.is_Completed);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = list.get(position);

        holder.name.setText(task.getName());
        holder.itemView.setBackgroundColor(activity.getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
        holder.comment.setText((task.getComment().length() > 16) ? task.getComment().substring(0, 16) + "..." : task.getComment());
        holder.isCompleted.setChecked(task.isCompleted());
        holder.isCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setCompleted(isChecked);
                App.getAuthController().addTaskToSubject(task, task.getName(), task.getSubject(), b -> {
                });
            }


        });


        holder.itemView.setOnClickListener(click -> {
            Intent intent = new Intent(activity, TaskActivity.class);
            intent.putExtra(App.TASK, task.getName());
            intent.putExtra(App.SUBJECT, task.getSubject());
            activity.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(activity).setTitle("Delete")
                    .setMessage("Вы действительно хотите удалить задание?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        dialog.dismiss();
                        App.getAuthController().removeTaskFromSubject(task.getName(), task.getSubject(), t -> {
                            list.remove(task);
                            notifyDataSetChanged();
                        });
                    }).setNegativeButton("Нет", (dialog, which) -> dialog.dismiss()).show();


            return true;
        });

    }
}