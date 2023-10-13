package com.example.school.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.school.Adapters.SubjectAdapter;
import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.example.school.R;
import com.example.school.databinding.FragmentDashboardBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;

import java.io.DataInput;
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
    ArrayList<String> allDays = new ArrayList<>();

    DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (App.isConnectedToNetwork()) {
            authController.getAllSubjectsFromDb(task -> {
                ArrayList<String> chosen_strings = new ArrayList<>();

                if (task.isSuccessful()) {
                    for (DataSnapshot e : task.getResult().getChildren()) {
                        if (!allSb_str.contains(e.getValue(Subject.class).getName()))
                            allSb_str.add(e.getValue(Subject.class).getName());
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

    SubjectAdapter subjectAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentDashboardBinding.bind(view);


//        if (App.isConnectedToNetwork()) {
            subjectAdapter = new SubjectAdapter(day.getSubjects(), getActivity());
            binding.daySubjects.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.daySubjects.setAdapter(subjectAdapter);


            date = dateFormat.format(new Date());

            authController.getDayFromDB(date, task -> {
                if (task.getResult().exists()) {
                    day = task.getResult().getValue(Day.class);
                } else {
                    day = new Day(date);
                    allDays.add(date);
                    authController.addDayToDb(day, g -> {
                    });
                }

            });
            authController.getSubjectsFromDay(date, l -> {
                for (DataSnapshot e : l.getResult().getChildren()) {
                    day.addSubject(e.getValue(Subject.class));
                    binding.dayName.setText(day.getDate());
                    subjectAdapter.setList(day.getSubjects());
                }
            });

            subjectAdapter.notifyDataSetChanged();

            Log.e("OH", day.getSubjects().size() + " ");


            binding.calendar.setOnClickListener(cl -> {

                MaterialDatePicker dates = MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build();
                dates.show(getActivity().getSupportFragmentManager(), "tag");
                dates.addOnPositiveButtonClickListener(selection -> {
                    date = String.valueOf(dates.getHeaderText());
                    if (allDays.contains(date)) {
                        authController.getDayFromDB(date, l -> {
                            day = l.getResult().getValue(Day.class);
                        });
                    } else {
                        authController.addDayToDb(new Day(date), l -> {
                            day = new Day(date);
                            allDays.add(date);
                        });
                    }
                    authController.getSubjectsFromDay(date, l -> {
                        for (DataSnapshot e : l.getResult().getChildren()) {
                            day.addSubject(e.getValue(Subject.class));
                            binding.dayName.setText(day.getDate());
                            subjectAdapter.setList(day.getSubjects());
                        }
                    });

                    subjectAdapter.notifyDataSetChanged();

                });
            });
            binding.hgh.setOnClickListener(df -> {

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_for_choose_sb_in_dashboard, R.id.name_of_subject_item_in_dashboard_choose, allSb_str);

                binding.listOfSubjects.setAdapter(adapter);


                binding.windowList.setVisibility(View.VISIBLE);
                binding.ok.setVisibility(View.GONE);

                binding.listOfSubjects.setOnItemClickListener((parent, view1, position, id) -> {
                    binding.windowList.setVisibility(View.GONE);
                    this.day.addSubject(allSb.get(position));


                    authController.addDayToDb(day, g -> {
                        subjectAdapter.setList(day.getSubjects());
                        subjectAdapter.notifyDataSetChanged();
                    });
                });
//                listView.setOnItemClickListener((parent, itemClicked, position, id) -> {
//                    SparseBooleanArray chosen = ((ListView) parent).getCheckedItemPositions();
//                    for (int i = 0; i<chosen.size(); i++){
//                        if (chosen.get(i)){
//                            if(!day.getSubjects().contains(allSb_str.get(i)))
//                                chosen_strings.add(allSb_str.get(i));
//                        }else{
//                            chosen_strings.remove(allSb_str.get(i));
//                        }
//                    }
//
//                });
//                binding.ok.setOnClickListener(c -> {
//                    binding.windowList.setVisibility(View.GONE);
//                    day.addSubjects(chosen_strings);
//                    authController.addDayToDb(day, allSb_str ->{});
//
//                  showSbFromDay();
//                });

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