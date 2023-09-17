package com.example.school;

import android.app.Application;

import com.example.school.Auth.AuthController;
import com.example.school.Logic.Day;
import com.example.school.Logic.Subject;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collection;

public class App extends Application {
    private static App instance;
    public ArrayList<Subject> usersSubject;

    public static ArrayList<String> getDays() {
        return days;
    }

    public static void setDays(ArrayList days) {
        App.days = days;
    }
    public static void setDayToDays(String day) {
        if(!App.days.contains(day))
            App.days.add(day);
    }

    public static ArrayList days = new ArrayList<>();
    static int[] colors_int =
            {R.color.sb_red,
                    R.color.sb_orange,
                    R.color.sb_yellow,
                    R.color.sb_green,
                    R.color.sb_blue,
                    R.color.sb_purple,
                    R.color.sb_brown};

    public static int[] getColors_int() {
        return colors_int;
    }

    public void setColors_int(int[] colors_int) {
        this.colors_int = colors_int;
    }

    public static String[] getColors_string() {
        return colors_string;
    }

    public void setColors_string(String[] colors_string) {
        this.colors_string = colors_string;
    }

    static String[] colors_string =
            {"Red",
            "Orange",
            "Yellow",
            "Green",
            "Blue",
            "Purple",
            "Brown"};

    final static String SUBJECT = "sb_ooo";

    public static AuthController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthController authController) {
        this.authController = authController;
    }

    public static AuthController authController = new AuthController();;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
