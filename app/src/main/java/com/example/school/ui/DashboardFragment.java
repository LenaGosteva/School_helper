package com.example.school.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.Adapters.AddSubjectToDayAdapter;
import com.example.school.Adapters.AddTaskToDayAdapter;
import com.example.school.Adapters.SubjectDayAdapter;
import com.example.school.Adapters.TaskAdapter;
import com.example.school.Adapters.TaskDayAdapter;
import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.databinding.FragmentDashboardBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    AuthController authController = new AuthController();
    String date;
    Day day = new Day();
    ArrayList<Subject> list_of_sb = new ArrayList<>();
    ArrayList<Subject> allSb = new ArrayList<>();
    ArrayList<Task> allTasks = new ArrayList<>();
    ArrayList<String> allSb_str = new ArrayList<>();
    ArrayList<String> allDays = new ArrayList<>();

    DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", new Locale("ru"));

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (App.isConnectedToNetwork()) {
            authController.getAllSubjectsFromDb(task -> {
                ArrayList<String> chosen_strings = new ArrayList<>();

                if (task.isSuccessful()) {
                    for (DataSnapshot e : task.getResult().getChildren()) {
                        if (!allSb_str.contains(e.getValue(Subject.class).getName()))
                            allSb_str.add(e.getValue(Subject.class).getName());
                        allTasks.addAll(e.getValue(Subject.class).getTasks().values());
                        if (!allSb.contains(e.getValue(Subject.class)))
                            allSb.add(e.getValue(Subject.class));
                    }
                }
                Log.e("IYF", allSb_str + "");
            });
            authController.getAllDaysFromDb(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot e : task.getResult().getChildren()) {
                        if (!allDays.contains(e.getValue(Day.class).getDate()))
                            allDays.add(e.getValue(Day.class).getDate());
                    }
                }
                Log.e("TRY", allDays + " ");


            });
        }

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    SubjectDayAdapter subjectDayAdapter;
    TaskDayAdapter taskAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardBinding.bind(view);


//        if (App.isConnectedToNetwork()) {
        date = dateFormat.format(new Date());
binding.dashboardFragmentWithInternet.setVisibility(View.VISIBLE);
        authController.getDayFromDB(date, task -> {
            if (task.getResult().exists()) {
                day = task.getResult().getValue(Day.class);
                binding.dayName.setText(day.getDate());
                binding.dayNameWindowList.setText(day.getDate());

            } else {
                day = new Day(date);
                allDays.add(date);
                binding.dayName.setText(day.getDate());
                authController.addSubjectsToDay(day.getSubjects(), date, g -> {
                });
                authController.addTasksToDay(day.getTasks(), date, ghjg -> {
                });
                Log.e("OH", day.getSubjects().size() + " ");

            }
            setListToRecyclerView();

        });
        authController.getSubjectsFromDay(date, l -> {
            for (DataSnapshot e : l.getResult().getChildren()) {
                day.addSubject(e.getValue(Subject.class));
            }
        });
        authController.getTasksFromDay(date, l -> {
            for (DataSnapshot e : l.getResult().getChildren()) {
                day.addTask(e.getValue(Task.class));
            }
        });


        binding.selectTackOrSubject.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               setListToRecyclerView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.calendar.setOnClickListener(cl -> {

            MaterialDatePicker dates = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
            dates.show(getActivity().getSupportFragmentManager(), "tag");
            dates.addOnPositiveButtonClickListener(selection -> {
                Date d = new Date((Long)dates.getSelection());

                date = dateFormat.format(d);
binding.dayName.setText(date);
                authController.getDayFromDB(date, task -> {
                    if (task.getResult().exists()) {
                        day = task.getResult().getValue(Day.class);
                    } else {
                        day = new Day(date);
                        allDays.add(date);
                        authController.addSubjectsToDay(day.getSubjects(), date, g -> {
                        });
                        authController.addTasksToDay(day.getTasks(), date, ghjg -> {
                        });

                    }
                    setListToRecyclerView();

                });
//
//                authController.getSubjectsFromDay(date, l -> {
//                    for (DataSnapshot e : l.getResult().getChildren()) {
//                        day.addSubject(e.getValue(Subject.class));
//                        subjectDayAdapter.setList(day.getSubjects());
//                    }
//                });
//                binding.dayName.setText(day.getDate());
//                if (binding.selectTackOrSubject.getSelectedTabPosition() == 1) {
//                    Log.e("agfd",day.getTasks().size()+"");
//                    taskAdapter = new TaskAdapter(day.getTasks(), getActivity());
//                    binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
//                    binding.daySubjects.setAdapter(taskAdapter);
//                    authController.addTasksToDay(taskAdapter.getList(),date, hjh->{});
//                } else {
//                    subjectDayAdapter = new SubjectDayAdapter(day, getActivity());
//                    binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
//                    binding.daySubjects.setAdapter(subjectDayAdapter);
//                    authController.addSubjectsToDay(subjectDayAdapter.getList(),date, hjh->{});
//
//                }
            });
        });
        binding.hgh.setOnClickListener(sdfg -> {

            binding.layoutAddSubjectToDay.setVisibility(binding.layoutAddSubjectToDay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            binding.layoutAddTaskToDay.setVisibility(binding.layoutAddTaskToDay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        binding.addSubjectToDay.setOnClickListener(df -> {
            if (allSb.size() != 0) {


                binding.windowList.setVisibility(View.VISIBLE);
                binding.ok.setVisibility(View.GONE);
                binding.dashboardFragmentWithInternet.setVisibility(View.GONE);

                binding.layoutAddSubjectToDay.setVisibility(View.GONE);
                binding.layoutAddTaskToDay.setVisibility(View.GONE);
                binding.hgh.setVisibility(View.GONE);

                AddSubjectToDayAdapter addSbAdapter = new AddSubjectToDayAdapter(allSb, this.getActivity(), binding, day);
                binding.listOfSubjects.setLayoutManager(new GridLayoutManager(getContext(), 2));
                binding.listOfSubjects.setAdapter(addSbAdapter);
            } else {

                Toast.makeText(getContext(), "Предметов нет((", Toast.LENGTH_SHORT).show();

            }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_for_choose_sb_in_dashboard, R.id.name_of_subject_item_in_dashboard_choose, allSb_str);
//                binding.listOfSubjects.setAdapter(adapter);
//
//                binding.listOfSubjects.setOnItemClickListener((parent, view1, position, id) -> {
//                    binding.windowList.setVisibility(View.GONE);
//
//
//
//
//                    this.day.addSubject(allSb.get(position));
//                    authController.addDayToDb(day, g -> {
//                        subjectDayAdapter.setList(day.getSubjects());
//                        subjectDayAdapter.notifyDataSetChanged();
//                    });
//                });
        });
        binding.addTaskToDay.setOnClickListener(dfg -> {
            if (allTasks.size() != 0) {


                binding.windowList.setVisibility(View.VISIBLE);
                binding.ok.setVisibility(View.GONE);

                binding.layoutAddSubjectToDay.setVisibility(View.GONE);
                binding.layoutAddTaskToDay.setVisibility(View.GONE);
                binding.hgh.setVisibility(View.GONE);
                AddTaskToDayAdapter addSbAdapter = new AddTaskToDayAdapter(allTasks, this.getActivity(), binding, day);
                binding.listOfSubjects.setLayoutManager(new GridLayoutManager(getContext(), 2));
                binding.listOfSubjects.setAdapter(addSbAdapter);
            } else {
                Toast.makeText(getContext(), "Заданий нет((", Toast.LENGTH_SHORT).show();
            }
        });
//        }
//        else{
//            binding.textNoInternet.setVisibility(View.VISIBLE);
//            binding.dashboardFragmentWithInternet.setVisibility(View.GONE
//            );
//        }

    }

    public void setListToRecyclerView(){
        if (binding.selectTackOrSubject.getSelectedTabPosition()==1) {
            Log.e("agfd", day.getTasks().size() + "");
            taskAdapter = new TaskDayAdapter(day.getTasks(), getActivity(), date);
            binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.daySubjects.setAdapter(taskAdapter);
        } else {
            subjectDayAdapter = new SubjectDayAdapter(day, getActivity());
            binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.daySubjects.setAdapter(subjectDayAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}