package com.example.school.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.App;
import com.example.school.Logic.Subject;
import com.example.school.R;
import com.example.school.databinding.ActivityListBinding;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {


    public ArrayList<Subject> getList() {
        return list;
    }

    public void setList(ArrayList<Subject> list) {
        this.list = list;
    }

    ArrayList<Subject> list;
    public final Activity activity;


    public SubjectAdapter(ArrayList<Subject> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_of_subjects, parent, false);

        view.setLongClickable(true);
        return new SubjectAdapter.SubjectViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView describtion;

        public SubjectViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_of_subject_item);
            describtion = itemView.findViewById(R.id.describtion_of_subject_item);


        }
    }

    @Override
    public void onBindViewHolder
            (@NonNull SubjectAdapter.SubjectViewHolder holder, int position) {
        Subject subject = list.get(position);

        holder.name.setText(subject.getName());
        holder.describtion.setText(subject.getDescription());
        holder.itemView.setBackgroundColor(activity.getColor(subject.getColor()));


        holder.itemView.setOnClickListener(click -> {

        });
    }


}
