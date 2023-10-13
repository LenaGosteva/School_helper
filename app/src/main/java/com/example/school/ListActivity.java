package com.example.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.school.Adapters.TaskAdapter;
import com.example.school.Logic.Task;
import com.example.school.databinding.ActivityListBinding;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    ArrayList<Task> tasks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (App.isConnectedToNetwork()) {
            getIntent().getExtras().getString(App.SUBJECT, "Additionally");

            App.authController.getAllTasksFromSubject(getIntent().getExtras().getString(App.SUBJECT, "Additionally"), listener -> {
                if (listener.isSuccessful()) {
                    for (DataSnapshot e : listener.getResult().getChildren()) {
                        tasks.add(e.getValue(Task.class));
                    }
                }
                TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
                if (tasks.size() == 0) {
                    binding.mainRlListActivity.setBackgroundColor(getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
                    binding.textNothing.setVisibility(View.VISIBLE);
                } else {
                    binding.listOfTasks.setLayoutManager(new LinearLayoutManager(this));
                    binding.listOfTasks.setAdapter(taskAdapter);
                }
            });


        } else {
            binding.mainRlListActivity.setBackgroundColor(getResources().getColor(R.color.sb_brown));
            binding.listOfTasks.setVisibility(View.GONE);
            binding.textNoInternet.setVisibility(View.VISIBLE);
        }
        binding.back.setOnClickListener(g -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}