package com.example.school;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.model.Model;
import com.example.school.Adapters.TaskAdapter;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.databinding.ActivityListBinding;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    ArrayList<Task> tasks = new ArrayList<>();
    int panic=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (App.isConnectedToNetwork()) {
            binding.nameOfSubjectListActivity.setText(getIntent().getExtras().getString(App.SUBJECT, "Additionally"));

            App.authController.getAllTasksFromSubject(getIntent().getExtras().getString(App.SUBJECT, "Additionally"), listener -> {
                if (listener.isSuccessful()) {
                    for (DataSnapshot e : listener.getResult().getChildren()) {
                        tasks.add(e.getValue(Task.class));
                    }
                }
                TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
                if (tasks.size() == 0) {
//                    binding.mainRlListActivity.setBackgroundColor(getIntent().getIntExtra(App.COLOR, R.color.sb_brown));
                    binding.textNothing.setVisibility(View.VISIBLE);
                } else {
                    binding.listOfTasks.setLayoutManager(new LinearLayoutManager(this));
                    binding.listOfTasks.setAdapter(taskAdapter);
                }
            });
        binding.add.setOnClickListener(add->{
            binding.windowForNewTaskListActivity.setVisibility(View.VISIBLE);
            binding.listOfTasks.setVisibility(View.GONE);
        });
            binding.newTaskListActivity.setOnClickListener(newt ->{


                String name = binding.nameOfTaskListActivity.getText().toString();
                String comment = binding.commentOfTaskListActivity.getText().toString().isEmpty()?"Комментарий задания":binding.commentOfTaskListActivity.getText().toString();
                String theory = binding.theoryOfTaskListActivity.getText().toString().isEmpty()?"Теория задания":binding.theoryOfTaskListActivity.getText().toString();
                String practise = binding.practiceOfTaskListActivity.getText().toString().isEmpty()?"Практика задания":binding.practiceOfTaskListActivity.getText().toString();
               boolean f = false;
                for (char c: App.getFirebase_symbols_ban()) {
                    String s_c = String.valueOf(c);
                    if (name.contains(s_c)||comment.contains(s_c)||theory.contains(s_c)||practise.contains(s_c)
                    ){
                        f = false;
                        Toast.makeText(this, "Одно из полей содержит неконвертируемые символы! ('.', '#', '$', '[', или ']')", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        f = true;
                    }
                }
                if (!name.isEmpty()&&f) {
                    if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId()==binding.notPanicListActivity.getId()){
                    panic = 0;
                } else if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId()==binding.panicListActivity.getId()) {
                    panic = 1;
                }else if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId()==binding.megaPanicListActivity.getId()) {
                    panic = 2;
                }
                    Task s = new Task(name,comment, theory, practise, getIntent().getExtras().getString(App.SUBJECT, "Additionally"), panic, false);
                    tasks.add(s);
                    TaskAdapter taskAdapter = new TaskAdapter(tasks, this);
                    binding.listOfTasks.setLayoutManager(new LinearLayoutManager(this));
                    binding.listOfTasks.setAdapter(taskAdapter);
                    App.getAuthController().addTaskToSubject(s, s.getName(), s.getSubject(), tas -> {
                        if (tas.isComplete()){
                            Toast.makeText(this, "Задание успешно добавлено!", Toast.LENGTH_SHORT).show();
                            binding.textNoInternet.setVisibility(View.GONE);
                            binding.windowForNewTaskListActivity.setVisibility(View.GONE);
                            binding.listOfTasks.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(this, "Задание не добавлено!", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else{
                    Toast.makeText(this, "Вы не ввели название!", Toast.LENGTH_SHORT).show();
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