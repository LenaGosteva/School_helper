package com.example.school.ui;

import static com.example.school.App.COLOR;
import static com.example.school.App.authController;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.school.Logic.Subject;
import com.example.school.MainActivity;
import com.example.school.R;
import com.example.school.databinding.FragmentRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Objects;


public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = com.example.school.databinding.FragmentRegistrationBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.enter.setOnClickListener(enter->{
            String email = String.valueOf(binding.inputEmail.getInputText());
            String name = String.valueOf(binding.inputNickname.getInputText());
            String password = String.valueOf(binding.inputPassword.getInputText());
            String repeat_password = String.valueOf(binding.repeatPassword.getInputText());
            if (!email.isEmpty() && !password.isEmpty()&& !name.isEmpty()&&(password.equals(repeat_password))) {

                        authController.registerUser(email, password, authResult -> {
                            if (authResult.isSuccessful()) {
                                authController.addUserToDb(name,task1 ->{
                                    startActivity(new Intent(getContext(), MainActivity.class));});
                            } else {
                                Toast.makeText(getContext(), Objects.requireNonNull(authResult.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this.getContext(), "Заполните обязательные поля!", Toast.LENGTH_SHORT).show();
            }

        });


    }
}