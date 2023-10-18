package com.example.school.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.example.school.R;

import java.util.ArrayList;

public class SubjectDayAdapter extends RecyclerView.Adapter<SubjectDayAdapter.SubjectViewHolder> {


    public ArrayList<Subject> getList() {
        return list;
    }

    public void setList(ArrayList<Subject> list) {
        this.list = list;
    }

    ArrayList<Subject> list;
    Day day;
    public final Activity activity;


    public SubjectDayAdapter(Day day, Activity activity) {
        this.day = day;
        this.list = day.getSubjects();
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_list_of_subjects, parent, false);

        view.setLongClickable(true);
        return new SubjectDayAdapter.SubjectViewHolder(view);
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
            (@NonNull SubjectDayAdapter.SubjectViewHolder holder, int position) {
        Subject subject = list.get(position);

        holder.name.setText(subject.getName());
        holder.describtion.setText(subject.getDescription().length() > 16 ? subject.getDescription().substring(0, 16) + "..." : subject.getDescription());
        holder.itemView.setBackgroundColor(activity.getColor(subject.getColor()));


        holder.itemView.setOnClickListener(click -> {
            if (App.isConnectedToNetwork()) {
                Intent intent = new Intent(activity, ListActivity.class);
                intent.putExtra(App.SUBJECT, subject.getName());
                intent.putExtra(App.COLOR, activity.getColor(subject.getColor()));
                Log.e("index_color_before_intent_to_list_of_task", String.valueOf(activity.getIntent().getIntExtra(App.COLOR, R.color.sb_brown)).toUpperCase());

                activity.startActivity(intent);
            } else {
                Toast.makeText(App.getInstance(), "Нет интернета, задания недоступны", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnLongClickListener(l -> {
            new AlertDialog.Builder(activity).setTitle("Удаление")
                    .setMessage("Вы действительно хотите удалить предмет? ")
                    .setPositiveButton("Да", (dialog, which) -> {
                        dialog.dismiss();
                        day.getSubjects().remove(subject);



                        list.remove(subject);
                        notifyDataSetChanged();

                        day.addSubjects(list);
                        App.authController.removeSubjectssFromDay(day.getDate(), n -> {
                            for (Subject s : list) {
                                App.authController.addSubjectToDay(s, String.valueOf(list.indexOf(s)), day.getDate(), kjl -> {
                                });
                            }
                        });
                    }).setNegativeButton("Нет", (dialog, which) -> dialog.dismiss()).show();


            return true;
        });
    }


}
