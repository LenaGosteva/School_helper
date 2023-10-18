package com.example.school.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.App;
import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.databinding.FragmentDashboardBinding;

import java.util.List;

public class AddTaskToDayAdapter extends RecyclerView.Adapter<AddTaskToDayAdapter.SubjectViewHolder> {

    FragmentDashboardBinding binding;
    Day day;

    public List<Task> getList() {
        return list;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    List<Task> list;
    public final Activity activity;


    public AddTaskToDayAdapter(List<Task> list, Activity activity, FragmentDashboardBinding binding, Day day) {
        this.list = list;
        this.activity = activity;
        this.binding = binding;
        this.day = day;
    }


    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_choose_sb_in_dashboard, parent, false);

        view.setLongClickable(true);
        return new AddTaskToDayAdapter.SubjectViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public SubjectViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_of_subject_item_in_dashboard_choose);


        }
    }

    @Override
    public void onBindViewHolder
            (@NonNull AddTaskToDayAdapter.SubjectViewHolder holder, int position) {
        Task task = list.get(position);
        holder.name.setText(task.getName());
        holder.itemView.setOnClickListener(click -> {
            day.addTask(task);

            App.getAuthController().addDayToDb(day, t -> {
                binding.windowList.setVisibility(View.GONE);
                binding.hgh.setVisibility(View.VISIBLE);

                binding.addSubjectToDay.setVisibility(View.GONE);
                binding.addTaskToDay.setVisibility(View.GONE);
                if (binding.selectTackOrSubject.getSelectedTabPosition()==1){
                TaskDayAdapter taskAdapter = new TaskDayAdapter(day, activity);
                binding.daySubjects.setAdapter(taskAdapter);
                taskAdapter.notifyDataSetChanged();}
            });
        });
    }


}
