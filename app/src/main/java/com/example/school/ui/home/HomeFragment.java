package com.example.school.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.school.Adapters.SubjectAdapter;
import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.example.school.R;
import com.example.school.databinding.FragmentHomeBinding;
import com.example.school.ui.LoginActivity;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.grpc.ManagedChannelProvider;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<Subject> test = new ArrayList<>();
    AuthController authController = new AuthController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);
        if (App.isConnectedToNetwork()) {
            AtomicReference<List<Subject>> list = new AtomicReference<>(new ArrayList<>());

            SubjectAdapter adapter = new SubjectAdapter(list.get(), getActivity());
            binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.list.setAdapter(adapter);


            authController.getAllSubjectsFromDb(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot e : task.getResult().getChildren()) {
                        list.get().add(e.getValue(Subject.class));
                    }
                }
                adapter.notifyDataSetChanged();

            });
            binding.name.setText(!authController.isAuth() ?
                    getResources().getString(R.string.name_of_user) : authController.getUser().getEmail());

            binding.out.setOnClickListener(click -> {
                new AlertDialog.Builder(getContext()).setTitle("Предупреждение")
                        .setMessage("Приложение работает только в случае авторизации. При выходе вы потеряете доступ к данным")
                        .setPositiveButton("Все равно выйти", (dialog, which) -> {
                            dialog.dismiss();
                            authController.singOut();

                            Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }).setNegativeButton("Тогда остаюсь", (dialog, which) -> dialog.dismiss()).show();


            });

            binding.add.setOnClickListener(cl -> {
                binding.windowForNew.setVisibility(View.VISIBLE);

            });

            ArrayAdapter<String> spinner_adapter
                    = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, App.getColors_string());
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerForColor.setAdapter(spinner_adapter);
            final int[] index_color = new int[1];

            String name_of_subject = String.valueOf(binding.nameOfSubject.getText());
            binding.spinnerForColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    index_color[0] = position;
                    binding.colorViewForColor.setCircleBackgroundColor(getActivity().getColor(App.getColors_int_fill()[position]));
                    binding.colorViewForColor.setBackgroundColor(getActivity().getColor(App.getColors_int_fill()[position]));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    index_color[0] = 0;
                }
            });


            binding.newSubject.setOnClickListener(sdf -> {

                String desc = binding.describtionOfSubject.getText().toString();
                String name = binding.nameOfSubject.getText().toString();
                boolean f = false;
                for (char c: App.getFirebase_symbols_ban()) {
                    String s_c = String.valueOf(c);
                    if (name.contains(s_c)||desc.contains(s_c)
                    ){
                        f = false;
                        Toast.makeText(getContext(), "Одно из полей содержит неконвертируемые символы! ('.', '#', '$', '[', или ']')", Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        f = true;
                    }
                }



                if (!name.isEmpty()&&f) {
                    Subject s = new Subject(name, desc.isEmpty() ? " " : desc, App.getColors_int_fill()[index_color[0]]);
                    list.get().add(s);
                    authController.addSubjectToDb(s, task -> {
                        if (task.isSuccessful()) {
                            List<Subject> l = new ArrayList<>();
                            authController.getAllSubjectsFromDb(task_ds -> {
                                for (DataSnapshot e : task_ds.getResult().getChildren()) {
                                    l.add(e.getValue(Subject.class));
                                }
                                adapter.setList(l);
                                Toast.makeText(getContext(), "Предмет успешно добавлен!", Toast.LENGTH_SHORT).show();
                                binding.windowForNew.setVisibility(View.GONE);

                            });
                        } else {
                            Toast.makeText(getContext(), "Предмет не добавлен!", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Вы не ввели название!", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();

            });

        }else{
            binding.textNoInternet.setVisibility(View.VISIBLE);
            binding.homeFragmentWithInternet.setVisibility(View.GONE
            );
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}