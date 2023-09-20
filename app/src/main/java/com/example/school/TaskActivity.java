package com.example.school;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.school.Auth.AuthController;
import com.example.school.Logic.Task;
import com.example.school.databinding.ActivityTaskBinding;

public class TaskActivity extends AppCompatActivity {
    ActivityTaskBinding binding;

    AuthController authController = new AuthController();
    Task task;
    String name;
    String subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(d -> {
//            startActivity(new Intent(this, ListActivity.class));
            finish();
        });

        name = getIntent().getExtras().getString(App.TASK);
        subject = getIntent().getExtras().getString(App.SUBJECT);
        authController.getTaskFromSubject(name, subject, t -> {
            task = t.getResult().getValue(Task.class);
            binding.name.setText(task.getName());
            binding.commT.setText(task.getComment());
            binding.isCompletedT.setChecked(task.isCompleted());
            binding.theoryT.setText(task.getTheory());
            binding.practiceT.setText(task.getPractice());
            binding.sbT.setText(subject);
            binding.allTaskAct.setBackgroundColor(getIntent().getIntExtra(App.COLOR, R.color.bright));
        });
        binding.isCompletedT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                authController.addCheckedToTask(isChecked, task.getName(), task.getSubject(), s -> {
                });
            }
        });
        binding.edit.setOnClickListener(d -> {
            binding.comm.showNext();
            binding.practice.showNext();
            binding.theory.showNext();
            binding.vpEa.showNext();
            binding.commE.setText(task.getComment());
            binding.theoryE.setText(task.getTheory());
            binding.practiceE.setText(task.getPractice());
        });
        binding.save.setOnClickListener(f -> {
            binding.comm.showPrevious();
            binding.practice.showPrevious();
            binding.theory.showPrevious();
            binding.vpEa.showPrevious();
            task.setComment(binding.commE.getText().toString().isEmpty() ? task.getComment() : binding.commE.getText().toString());
            task.setTheory(binding.theoryE.getText().toString().isEmpty() ? task.getTheory() : binding.theoryE.getText().toString());
            task.setPractice(binding.practiceE.getText().toString().isEmpty() ? task.getPractice() : binding.practiceE.getText().toString());
            binding.commT.setText(task.getComment());
            binding.theoryT.setText(task.getTheory());
            binding.practiceT.setText(task.getPractice());
            authController.addTaskToSubject(task, task.getName(), task.getSubject(), h -> {
            });
        });

    }
}