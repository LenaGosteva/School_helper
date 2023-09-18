package com.example.school.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.example.school.Logic.Task;
import com.example.school.R;
import com.example.school.databinding.FragmentNewTaskBinding;
import com.example.school.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class NewTaskFragment extends Fragment {

    private FragmentNewTaskBinding binding;
    AuthController authController = new AuthController();
    ArrayList<String> allSb_str = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_new_task, container, false);
    }

    String name, comment = " ", theory = " ", practice = " ", subject = "Additionally";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentNewTaskBinding.bind(view);
        authController.getAllSubjectsFromDb(task -> {
            ArrayList<String> chosen_strings = new ArrayList<>();

            if (task.isSuccessful()) {
                for (DataSnapshot e : task.getResult().getChildren()) {
                    if (!allSb_str.contains(e.getValue(Subject.class).getName()))
                        allSb_str.add(e.getValue(Subject.class).getName());

                }
                ArrayAdapter<String> spinner_adapter
                        = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, allSb_str);
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.subjectOfTask.setAdapter(spinner_adapter);
                binding.subjectOfTask.setSelection(allSb_str.indexOf("Additionally"));

                Log.e("IF", allSb_str + "");

                binding.subjectOfTask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        subject = allSb_str.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        subject = "Additionally";
                    }


                });
            }
            binding.addTask.setOnClickListener(s -> {
                if (binding.nameOfTask.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Вы забыли про название!", Toast.LENGTH_SHORT).show();
                } else {
                    name = binding.nameOfTask.getText().toString();
                    comment = binding.commentOfTask.getText().toString().isEmpty() ? "This is comment" : binding.commentOfTask.getText().toString();
                    theory = binding.theoryOfTask.getText().toString().isEmpty() ? "This is comment" : binding.theoryOfTask.getText().toString();
                    practice = binding.practiceOfTask.getText().toString().isEmpty() ? "This is comment" : binding.practiceOfTask.getText().toString();
                    authController.addTaskToSubject(new Task(name, comment, theory, practice, subject), name, subject, l -> {
                        binding.nameOfTask.setText("");
                        binding.commentOfTask.setText("");
                        binding.theoryOfTask.setText("");
                        binding.practiceOfTask.setText("");
                         });
                }
            });
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}