package com.example.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

        String sb = new Intent().getExtras().getString(App.SUBJECT);



        App.authController.getAllTasksFromSubject(sb, listener -> {
            if (listener.isSuccessful()) {
                for (DataSnapshot e : listener.getResult().getChildren()) {
                    tasks.add(e.getValue(Task.class));
            }}
        });


        TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
        binding.listOfTasks.setAdapter(taskAdapter);

    }
}