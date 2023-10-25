package com.example.school;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.model.Model;
import com.example.school.Adapters.TaskAdapter;
import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.databinding.ActivityListBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    public ArrayList<Task> tasks = new ArrayList<>();
    public ArrayList<Task> tasks_completed = new ArrayList<>();
    public ArrayList<Task> tasks_notCompleted = new ArrayList<>();
    int panic = 0;
    TaskAdapter taskAdapter;

    public ActivityListBinding getBinding() {
        return binding;
    }

    public void setBinding(ActivityListBinding binding) {
        this.binding = binding;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Task> getTasks_completed() {
        return tasks_completed;
    }

    public void setTasks_completed(ArrayList<Task> tasks_completed) {
        this.tasks_completed = tasks_completed;
    }

    public ArrayList<Task> getTasks_notCompleted() {
        return tasks_notCompleted;
    }

    public void setTasks_notCompleted(ArrayList<Task> tasks_notCompleted) {
        this.tasks_notCompleted = tasks_notCompleted;
    }

    public int getPanic() {
        return panic;
    }

    public void setPanic(int panic) {
        this.panic = panic;
    }

    public TaskAdapter getTaskAdapter() {
        return taskAdapter;
    }

    public void setTaskAdapter(TaskAdapter taskAdapter) {
        this.taskAdapter = taskAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.listOfTasks.setLayoutManager(new LinearLayoutManager(this));

        binding.nameOfSubjectListActivity.setClickable(true);
        binding.nameOfSubjectListActivity.setOnClickListener(ghjk->{
            App.authController.getSubjectFromDB(getIntent().getExtras().getString(App.SUBJECT, "Additionally"),yguh->{;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, App.gson.toJson(yguh.getResult().getValue(Subject.class))); // текст отправки
                startActivity(Intent.createChooser(intent, "Share with"));
                Log.e("GFIHVIH",  App.gson.toJson(yguh.getResult().getValue(Subject.class)));
            });

        });

        if (App.isConnectedToNetwork()) {
            binding.nameOfSubjectListActivity.setText(getIntent().getExtras().getString(App.SUBJECT, "Additionally"));

            App.authController.getAllTasksFromSubject(getIntent().getExtras().getString(App.SUBJECT, "Additionally"), listener -> {
                if (listener.isSuccessful()) {
                    for (DataSnapshot e : listener.getResult().getChildren()) {
                        Task n = e.getValue(Task.class);
                        tasks.add(n);
                        assert n != null;
                        if (n.isCompleted()) {
                            tasks_completed.add(n);
                        } else {
                            tasks_notCompleted.add(n);
                        }
                    }
                }
                setRecyclerList();
            });
            binding.tabCheckedCompleting.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setRecyclerList();

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            binding.add.setOnClickListener(add -> {
                binding.windowForNewTaskListActivity.setVisibility(View.VISIBLE);
                binding.listOfTasks.setVisibility(View.GONE);

            });
            binding.newTaskListActivity.setOnClickListener(newt -> {


                String name = binding.nameOfTaskListActivity.getText().toString();
                String comment = binding.commentOfTaskListActivity.getText().toString().isEmpty() ? "Комментарий задания" : binding.commentOfTaskListActivity.getText().toString();
                String theory = binding.theoryOfTaskListActivity.getText().toString().isEmpty() ? "Теория задания" : binding.theoryOfTaskListActivity.getText().toString();
                String practise = binding.practiceOfTaskListActivity.getText().toString().isEmpty() ? "Практика задания" : binding.practiceOfTaskListActivity.getText().toString();
                boolean f = false;
                for (char c : App.getFirebase_symbols_ban()) {
                    String s_c = String.valueOf(c);
                    if (name.contains(s_c) || comment.contains(s_c) || theory.contains(s_c) || practise.contains(s_c)
                    ) {
                        f = false;
                        Toast.makeText(this, "Одно из полей содержит неконвертируемые символы! ('.', '#', '$', '[', или ']')", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        f = true;
                    }
                }
                if (!name.isEmpty() && f) {
                    if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId() == binding.notPanicListActivity.getId()) {
                        panic = 2;
                    } else if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId() == binding.panicListActivity.getId()) {
                        panic = 1;
                    } else if (binding.radioGroupPanicListActivity.getCheckedRadioButtonId() == binding.megaPanicListActivity.getId()) {
                        panic = 0;
                    }
                    Task s = new Task(name, comment, theory, practise, getIntent().getExtras().getString(App.SUBJECT, "Additionally"), getIntent().getExtras().getInt(App.COLOR), panic, false);
                    tasks.add(s);
                    tasks_notCompleted.add(s);
                    setRecyclerList();
                    App.getAuthController().addTaskToSubject(s, s.getName(), s.getSubject(), tas -> {
                        if (tas.isComplete()) {
                            Toast.makeText(this, "Задание успешно добавлено!", Toast.LENGTH_SHORT).show();
                            binding.textNoInternet.setVisibility(View.GONE);
                            binding.windowForNewTaskListActivity.setVisibility(View.GONE);
                            binding.listOfTasks.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(this, "Задание не добавлено!", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
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

    public void setRecyclerList() {
        if (binding.tabCheckedCompleting.getSelectedTabPosition() == 0) {
            if (tasks_notCompleted.size() == 0) {
                binding.textNothing.setVisibility(View.VISIBLE);
                binding.listOfTasks.setVisibility(View.GONE);
            } else {
                taskAdapter = new TaskAdapter(tasks_notCompleted, tasks_completed, this, binding);
                binding.listOfTasks.setAdapter(taskAdapter);
                binding.listOfTasks.setVisibility(View.VISIBLE);
                binding.textNothing.setVisibility(View.GONE);


            }

        } else {
            if (tasks_completed.size() == 0) {
                binding.textNothing.setVisibility(View.VISIBLE);
                binding.listOfTasks.setVisibility(View.GONE);

            } else {
                taskAdapter = new TaskAdapter(tasks_completed, tasks_notCompleted, this, binding);
                binding.listOfTasks.setAdapter(taskAdapter);
                binding.listOfTasks.setVisibility(View.VISIBLE);

                binding.textNothing.setVisibility(View.GONE);

            }
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//
//    }
}