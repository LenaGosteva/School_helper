package com.example.school.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.App;
import com.example.school.ListActivity;
import com.example.school.Logic.Subject;
import com.example.school.R;
import com.example.school.databinding.FragmentDashboardBinding;
import com.example.school.databinding.ListToAddSubjectsToDayBinding;

import java.util.ArrayList;

public class AddSubjectToDayAdapter extends RecyclerView.Adapter<AddSubjectToDayAdapter.SubjectViewHolder> {

FragmentDashboardBinding binding;
    public ArrayList<Subject> getList() {
        return list;
    }

    public void setList(ArrayList<Subject> list) {
        this.list = list;
    }

    ArrayList<Subject> list;
    public final Activity activity;


    public AddSubjectToDayAdapter(ArrayList<Subject> list, Activity activity,FragmentDashboardBinding binding) {
        this.list = list;
        this.activity = activity;
        this.binding = binding;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_choose_sb_in_dashboard, parent, false);

        view.setLongClickable(true);
        return new AddSubjectToDayAdapter.SubjectViewHolder(view);
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
            (@NonNull AddSubjectToDayAdapter.SubjectViewHolder holder, int position) {
        Subject subject = list.get(position);
        holder.itemView.setOnClickListener(click -> {

        });
    }


}
