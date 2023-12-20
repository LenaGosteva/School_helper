package com.example.school;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.school.Auth.AuthController;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.school.databinding.ActivityMainBinding;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class MainActivity extends AppCompatActivity implements ColorPickerDialogListener {

    public static int color;
    private ActivityMainBinding binding;
    AuthController authController = new AuthController();
    AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (!authController.isAuth()) {
            startActivity(new Intent(this, EnterActivity.class));
            finish();
        } else {


            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupWithNavController(binding.navView, navController);
        }
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        this.color = color;

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (findViewById(R.id.window_for_color_wheel).getVisibility() == View.VISIBLE)
                findViewById(R.id.window_for_color_wheel).setVisibility(View.GONE);
            else if (findViewById(R.id.window_for_import).getVisibility() == View.VISIBLE)
                findViewById(R.id.window_for_import).setVisibility(View.GONE);
            else if (findViewById(R.id.window_for_new).getVisibility() == View.VISIBLE)
                findViewById(R.id.window_for_new).setVisibility(View.GONE);
            else if(findViewById(R.id.window_list).getVisibility()==View.VISIBLE) {
                findViewById(R.id.window_list).setVisibility(View.GONE);
                findViewById(R.id.dashboard_fragment_with_internet).setVisibility(View.VISIBLE);
            }return true;
        } else {
            onBackPressed();
            return super.onKeyDown(keyCode, event);


        }
    }
}