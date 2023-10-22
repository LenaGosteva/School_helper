package com.example.school.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.example.school.MainActivity;
import com.example.school.R;
import com.example.school.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AuthController authController = new AuthController();
        binding.enter.setOnClickListener(enter->{
            String email = String.valueOf(binding.email.getInputText());
            String password = String.valueOf(binding.password.getInputText());
            if (!email.isEmpty() && !password.isEmpty()) {
                authController.enterUser(email, password, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });
            } else {
                Toast.makeText(this.getContext(), "Заполните обязательные поля!", Toast.LENGTH_SHORT).show();
            }

        });

        binding.forgot.setOnClickListener(pass -> {
            if (!binding.email.getInputText().isEmpty()) {

                App.authController.sendMailWithNewPassword(binding.email.getInputText(), task -> {
                    if (task.isSuccessful())
                        Toast.makeText(getContext(), "Письмо успешно отправлено", Toast.LENGTH_LONG).show();
                    else if (task.isCanceled())
                        Toast.makeText(getContext(), "Упс! Кажется, что-то пошло не так...", Toast.LENGTH_LONG).show();
                });
            }else{
                Toast.makeText(getContext(), "Введите почту и попробуйте снова!", Toast.LENGTH_SHORT).show();
            }
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Изменение пароля").setMessage("Введите почту");


//            final EditText input = new EditText(getApplicationContext());
//            input.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT));
//            alertDialog.setView(input)
//                    .setPositiveButton("Изменить",
//                            (dialog, which) -> {
//                                authController.sendMailWithNewPassword(input.getText().toString(), task -> {
//                                    if (task.isSuccessful())
//                                        Toast.makeText(App.getInstance().getApplicationContext(), "Письмо успешно отправлено", Toast.LENGTH_LONG).show();
//                                    else if (task.isCanceled())
//                                        Toast.makeText(App.getInstance().getApplicationContext(), "Упс! Кажется, что-то пошло не так...", Toast.LENGTH_LONG).show();
//                                });
//                                dialog.dismiss();
//                            })
//                    .setNegativeButton("Закрыть",
//                            (dialog, which) -> dialog.cancel())
//                    .show();
//
//
        });







    }
}