package com.example.school.ui.home;

import static android.content.Intent.getIntent;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rxcolorwheel.RXColorWheel;
import com.example.school.Adapters.SubjectAdapter;
import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.EnterActivity;
import com.example.school.Logic.Subject;
import com.example.school.MainActivity;
import com.example.school.R;
import com.example.school.databinding.FragmentHomeBinding;
import com.example.school.ui.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import io.grpc.ManagedChannelProvider;

public class HomeFragment extends Fragment{
    ColorPickerDialog colorPickerDialog = new ColorPickerDialog();

    private FragmentHomeBinding binding;
    ArrayList<Subject> test = new ArrayList<>();
    AuthController authController = new AuthController();
    String s;
    static int color = 2131034870;

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
            AtomicReference<ArrayList<Subject>> list = new AtomicReference<>(new ArrayList<>());
            SubjectAdapter adapter = new SubjectAdapter(list.get(), getActivity());
            binding.list.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.list.setAdapter(adapter);



            Uri uri = getActivity().getIntent().getData();

            // checking if the uri is null or not.
            if (uri != null) {

                // if the uri is not null then we are getting
                // the path segments and storing it in list.
                List<String> parameters = uri.getPathSegments();

                // after that we are extracting string
                // from that parameters.
                String param = parameters.get(parameters.size() - 1);

                // on below line we are setting that string
                // to our text view which we got as params.
                binding.name.setText(param);
            }




            authController.getName(hghjk -> {
                s = hghjk.getResult().getValue().toString();
                binding.name.setText(s);
            });
            authController.getAllSubjectsFromDb(task -> {
                if (task.isSuccessful()) {
                    for (DataSnapshot e : task.getResult().getChildren()) {
                        list.get().add(e.getValue(Subject.class));
                    }
                }
                adapter.notifyDataSetChanged();

            });
            binding.name.setText(!authController.isAuth() ?
                    getResources().getString(R.string.name_of_user) : s);

            binding.out.setOnClickListener(click -> {
                new AlertDialog.Builder(getContext()).setTitle("Предупреждение")
                        .setMessage("Приложение работает только в случае авторизации. При выходе вы потеряете доступ к данным")
                        .setPositiveButton("Все равно выйти", (dialog, which) -> {
                            dialog.dismiss();
                            authController.singOut();

                            Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), EnterActivity.class));
                        }).setNegativeButton("Тогда остаюсь", (dialog, which) -> dialog.dismiss()).show();
//
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, "http://school/hello"); // текст отправки
//                startActivity(Intent.createChooser(intent, "Share with"));
            });

            binding.addSubject.setOnClickListener(cl -> {
                binding.windowForNew.setVisibility(View.VISIBLE);
                binding.layoutAddSubject.setVisibility(View.GONE);
                binding.layoutImportSubject.setVisibility(View.GONE);
                binding.nameOfSubject.setText("" );
                binding.describtionOfSubject.setText("" );
            });
            binding.importSubject.setOnClickListener(cl->{
                binding.windowForImport.setVisibility(View.VISIBLE);
                binding.layoutAddSubject.setVisibility(View.GONE);
                binding.layoutImportSubject.setVisibility(View.GONE);
                binding.editImport.setText("");
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
            binding.add.setOnClickListener(sdfg -> {
                binding.layoutAddSubject.setVisibility(binding.layoutAddSubject.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                binding.layoutImportSubject.setVisibility(binding.layoutImportSubject.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });

            binding.newSubject.setOnClickListener(sdf -> {
                String desc = binding.describtionOfSubject.getText().toString();
                String name = binding.nameOfSubject.getText().toString();
                if (!name.isEmpty() && notConverted(desc, name)) {
                    Subject s = new Subject(name, desc.isEmpty() ? " " : desc, HomeFragment.color);
                    list.get().add(s);
                    authController.addSubjectToDb(s, task -> {
                        if (task.isSuccessful()) {
                            ArrayList<Subject> l = new ArrayList<>();
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
            binding.newImportSubject.setOnClickListener(sdf -> {

                String newSb = binding.editImport.getText().toString();
                if (!newSb.isEmpty()) {
                    try {
                        Subject s = App.gson.fromJson(newSb, Subject.class);
                        list.get().add(s);
                        authController.addSubjectToDb(s, task -> {
                            if (task.isSuccessful()) {
                                ArrayList<Subject> l = new ArrayList<>();
                                authController.getAllSubjectsFromDb(task_ds -> {
                                    for (DataSnapshot e : task_ds.getResult().getChildren()) {
                                        l.add(e.getValue(Subject.class));
                                    }
                                    adapter.setList(l);
                                    Toast.makeText(getContext(), "Предмет успешно добавлен!", Toast.LENGTH_SHORT).show();
                                    binding.windowForImport.setVisibility(View.GONE);

                                });
                            } else {
                                Toast.makeText(getContext(), "Предмет не добавлен!", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }catch (Exception e){
                        Toast.makeText(getContext(), "Предмет не добавлен!", Toast.LENGTH_SHORT).show();
                        binding.windowForImport.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(getContext(), "Вы ничего не ввели!", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();

            });
            binding.instrToSpinner.setClickable(true);
            binding.instrToSpinner.setOnClickListener(ghj -> {
                View d = getActivity().getCurrentFocus();
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(d.getWindowToken(), 0);
                }
                binding.windowForColorWheel.setVisibility(View.VISIBLE);

                binding.colorWheel.setColorChangeListener(new RXColorWheel.ColorChagneListener() {
                    @Override
                    public void onColorChanged(int color) {

                        binding.colorWheelOk.setOnClickListener(dfghj->{
                            binding.windowForColorWheel.setVisibility(View.GONE);
                            HomeFragment.color = color;
                            binding.colorViewForColor.setBackgroundColor(color);
                            binding.instrToSpinner.setTextColor(color);
                        });

                    }

                    @Override
                    public void firstDraw(int color) {
                    }
                });
                ;
            });
        } else {
            binding.textNoInternet.setVisibility(View.VISIBLE);
            binding.homeFragmentWithInternet.setVisibility(View.GONE);
        }

    }

public boolean notConverted(String... strings){
    boolean f = false;
    for (char c : App.getFirebase_symbols_ban()) {
        String s_c = String.valueOf(c);
        for (String s: strings) {
            if (s.contains(s_c)) {
                f = false;
                Toast.makeText(getContext(), "Одно из полей содержит неконвертируемые символы! ('.', '#', '$', '[', или ']')", Toast.LENGTH_SHORT).show();
                return f;
            } else {
                f = true;
            }
        }
    }       return f;

}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}