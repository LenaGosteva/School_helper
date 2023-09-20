package com.example.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.school.Adapters.TaskAdapter;
import com.example.school.Logic.Subject;
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
        getIntent().getExtras().getString(App.SUBJECT, "Additionally");

        App.authController.getAllTasksFromSubject(getIntent().getExtras().getString(App.SUBJECT, "Additionally"), listener -> {
            if (listener.isSuccessful()) {
                for (DataSnapshot e : listener.getResult().getChildren()) {
                    tasks.add(e.getValue(Task.class));
            }}
            TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
            if (tasks.size()==0){
                binding.main.setBackgroundColor(getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
                binding.textNothing.setVisibility(View.VISIBLE);
            }else{
            binding.listOfTasks.setLayoutManager(new LinearLayoutManager(this));
            binding.listOfTasks.setAdapter(taskAdapter);}
        });


        binding.back.setOnClickListener(g->{
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