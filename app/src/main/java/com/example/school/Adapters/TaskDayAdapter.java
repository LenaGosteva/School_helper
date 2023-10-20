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
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.TaskActivity;

import java.util.ArrayList;
import java.util.Comparator;

public class TaskDayAdapter extends RecyclerView.Adapter<TaskDayAdapter.TaskViewHolder> {

    public final Activity activity;
    ArrayList<Task> list;

    public TaskDayAdapter(ArrayList<Task> list, Activity activity) {
        list.sort(Comparator.comparing(Task::isCompleted).thenComparing(Task::getPanic));
        this.list = list;
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

    public void setList(ArrayList<Task> tasks) {
        this.list = tasks;
    }

    public ArrayList<Task> getList() {
        return this.list;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;
        public View curtain;
        public CheckBox isCompleted;
        public View panic;
        public View mega_panic;
        View  color;

        public TaskViewHolder(View itemView) {
            super(itemView);

            panic = itemView.findViewById(R.id.panic);
            mega_panic = itemView.findViewById(R.id.mega_panic);
            name = itemView.findViewById(R.id.name_of_task);
            curtain = itemView.findViewById(R.id.curtain_for_completed_task_item);
            comment = itemView.findViewById(R.id.comment_of_task);
            isCompleted = itemView.findViewById(R.id.is_Completed);
            color = itemView.findViewById(R.id.task_color_chip);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = list.get(position);
            if (task.getPanic() == 1) {
                holder.panic.setVisibility(View.VISIBLE);
            } else if (task.getPanic()==0){
                holder.mega_panic.setVisibility(View.VISIBLE);
            }
        holder.color.setBackgroundColor(
                task.getColor());
        holder.itemView.setBackground(task.isCompleted() ? activity.getDrawable(R.drawable.recycler_tasks_curtain):activity.getDrawable(R.drawable.recycler_tasks));

        holder.name.setText(task.getName());
        holder.comment.setText((task.getComment().length() > 24) ? task.getComment().substring(0, 24) + "..." : task.getComment());
        holder.isCompleted.setChecked(task.isCompleted());


        holder.isCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
list.remove(task);
if (isChecked){

}
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