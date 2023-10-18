package com.example.school.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school.Adapters.AddSubjectToDayAdapter;
import com.example.school.Adapters.AddTaskToDayAdapter;
import com.example.school.Adapters.SubjectDayAdapter;
import com.example.school.Adapters.TaskAdapter;
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

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    AuthController authController = new AuthController();
    String date;
    Day day = new Day();
    ArrayList<Subject> list_of_sb = new ArrayList<>();
    ArrayList<Subject> allSb = new ArrayList<>();
    ArrayList<String> allSb_str = new ArrayList<>();
    ArrayList<Task> allTasks = new ArrayList<>();
    ArrayList<Task> allTasks_str = new ArrayList<>();
    ArrayList<String> allDays = new ArrayList<>();


    DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");

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
    TaskAdapter taskAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardBinding.bind(view);


        binding.selectTackOrSubject.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                if (index==1){
                    taskAdapter = new TaskAdapter(day.getTasks(), getActivity());
                    binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.daySubjects.setAdapter(taskAdapter);
                }else{
                    subjectDayAdapter = new SubjectDayAdapter(day, getActivity());
                    binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.daySubjects.setAdapter(subjectDayAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        if (App.isConnectedToNetwork()) {
            subjectDayAdapter = new SubjectDayAdapter(day, getActivity());
            binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.daySubjects.setAdapter(subjectDayAdapter);


            date = dateFormat.format(new Date());

            authController.getDayFromDB(date, task -> {
                if (task.getResult().exists()) {
                    day = task.getResult().getValue(Day.class);
                    binding.dayName.setText(day.getDate());

                } else {
                    day = new Day(date);
                    allDays.add(date);
                    binding.dayName.setText(day.getDate());
                    authController.addDayToDb(day, g -> {
                    });
                }

            });
            authController.getSubjectsFromDay(date, l -> {
                for (DataSnapshot e : l.getResult().getChildren()) {
                    day.addSubject(e.getValue(Subject.class));
                    subjectDayAdapter.setList(day.getSubjects());
                }
            });

            subjectDayAdapter.notifyDataSetChanged();

            Log.e("OH", day.getSubjects().size() + " ");


            binding.calendar.setOnClickListener(cl -> {

                MaterialDatePicker dates = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
                dates.show(getActivity().getSupportFragmentManager(), "tag");
                dates.addOnPositiveButtonClickListener(selection -> {
                    Date d = new Date((Long) dates.getSelection());

                    date = dateFormat.format(d);
                    if (allDays.contains(dates.getSelection())) {
                        authController.getDayFromDB(date, l -> {
                            day = l.getResult().getValue(Day.class);
                            if (l.getResult().exists()) {
                                day = l.getResult().getValue(Day.class);
                                binding.dayName.setText(day.getDate());

                            } else {
                                day = new Day(date);
                                allDays.add(date);
                                binding.dayName.setText(day.getDate());
                                authController.addDayToDb(day, g -> {
                                });
                            }
                        });
                    } else {
                        authController.addDayToDb(new Day(date), l -> {
                            day = new Day(date);
                            allDays.add(date);
                            binding.dayName.setText(day.getDate());

                        });
                    }
                    authController.getSubjectsFromDay(date, l -> {
                        for (DataSnapshot e : l.getResult().getChildren()) {
                            day.addSubject(e.getValue(Subject.class));
                            subjectDayAdapter.setList(day.getSubjects());
                        }
                    });
                    binding.dayName.setText(day.getDate());
                    subjectDayAdapter.notifyDataSetChanged();

                });
            });



            binding.addSubjectToDay.setOnClickListener(df -> {
                binding.windowList.setVisibility(View.VISIBLE);
                binding.ok.setVisibility(View.GONE);
                binding.hgh.setVisibility(View.GONE);

                AddSubjectToDayAdapter addSbAdapter = new AddSubjectToDayAdapter(allSb, this.getActivity(), binding,day);
                binding.listOfSubjects.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                binding.listOfSubjects.setAdapter(addSbAdapter);
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
//                })
            });
            binding.addTaskToDay.setOnClickListener(klk->{
                binding.windowList.setVisibility(View.VISIBLE);
                binding.ok.setVisibility(View.GONE);
                binding.hgh.setVisibility(View.GONE);

                AddTaskToDayAdapter addSbAdapter = new AddTaskToDayAdapter(allTasks, this.getActivity(), binding,day);
                binding.listOfSubjects.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                binding.listOfSubjects.setAdapter(addSbAdapter);
            });
            binding.hgh.setOnClickListener(b->{
                binding.addSubjectToDay.setVisibility(View.VISIBLE);
                binding.addTaskToDay.setVisibility(View.VISIBLE);
            });
//        }
//        else{
//            binding.textNoInternet.setVisibility(View.VISIBLE);
//            binding.dashboardFragmentWithInternet.setVisibility(View.GONE
//            );
//        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}