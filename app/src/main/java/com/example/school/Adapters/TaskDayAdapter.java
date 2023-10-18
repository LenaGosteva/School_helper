package com.example.school.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.App;
import com.example.school.Logic.Day;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.TaskActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskDayAdapter extends RecyclerView.Adapter<TaskDayAdapter.TaskViewHolder> {

    public final Activity activity;
    List<Task> list;
    Day day;

    public TaskDayAdapter(Day day, Activity activity) {
        this.list = day.getTasks();
        this.day = day;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_of_tasks, parent, false);

        view.setLongClickable(true);
        return new TaskDayAdapter.TaskViewHolder(view);
    }

    Dialog dialog;


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;
        public View curtain;
        public CheckBox isCompleted;

        public TaskViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_of_task);
            curtain = itemView.findViewById(R.id.curtain_for_completed_task_item);
            comment = itemView.findViewById(R.id.comment_of_task);
            isCompleted = itemView.findViewById(R.id.is_Completed);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = list.get(position);


        holder.itemView.setBackgroundColor(
                activity.getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
        holder.curtain.setVisibility(task.isCompleted() ? View.VISIBLE : View.GONE);

        holder.name.setText(task.getName());
        holder.comment.setText((task.getComment().length() > 24) ? task.getComment().substring(0, 24) + "..." : task.getComment());
        holder.isCompleted.setChecked(task.isCompleted());


        holder.isCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            holder.itemView.setBackgroundColor(
                    activity.getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
            holder.curtain.setVisibility(task.isCompleted() ? View.VISIBLE : View.GONE);

            App.getAuthController().addTaskToSubject(task, task.getName(), task.getSubject(), b -> {
            });

        });

        holder.itemView.setOnClickListener(click -> {
            Intent intent = new Intent(activity, TaskActivity.class);
            intent.putExtra(App.TASK, task.getName());
            intent.putExtra(App.SUBJECT, task.getSubject());
            intent.putExtra(App.COLOR, activity.getIntent().getIntExtra(App.COLOR, R.color.bright));
            activity.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(activity).setTitle("Удаление")
                    .setMessage("Вы действительно хотите удалить предмет? ")
                    .setPositiveButton("Да", (dialog, which) -> {
                        dialog.dismiss();
                        list.remove(task);
                        notifyDataSetChanged();

                        day.addTasks(list);
                        App.authController.removeTasksFromDay(day.getDate(), n -> {
                            for (Task s : list) {
                                App.authController.addTaskToDay(s.getName(), String.valueOf(list.indexOf(s)), day.getDate(), kjl -> {

                                });
                            }
                        });
                    }).setNegativeButton("Нет", (dialog, which) -> dialog.dismiss()).show();


            return true;
        });

    }
}