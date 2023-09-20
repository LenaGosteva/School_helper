package com.example.school;

import android.app.Application;

import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class App extends Application {
    private static App instance;
    public ArrayList<Subject> usersSubject;


    public static ArrayList days = new ArrayList<>();
    static int[] colors_int =
            {R.color.sb_red,
                    R.color.sb_orange,
                    R.color.sb_yellow,
                    R.color.sb_green,
                    R.color.sb_blue,
                    R.color.sb_purple,
                    R.color.sb_brown};

    public static ArrayList<String> allSb_str = new ArrayList<>();

    static String[] colors_string =
            {"Red",
                    "Orange",
                    "Yellow",
                    "Green",
                    "Blue",
                    "Purple",
                    "Brown"};

    final public static String SUBJECT = "sb_ooo";


    final public static String COLOR = "color_ooo";
    final public static String TASK = "task_ooo";
    public static AuthController authController = new AuthController();

    public static AuthController getAuthController() {
        return authController;
    }

    public static void setInstance(App instance) {
        App.instance = instance;
    }

    public static int[] getColors_int() {
        return colors_int;
    }

    public static ArrayList<String> getDays() {
        return days;
    }

    public static void setDays(ArrayList days) {
        App.days = days;
    }

    public static void setDayToDays(String day) {
        if (!App.days.contains(day))
            App.days.add(day);
    }

    public void setColors_int(int[] colors_int) {
        App.colors_int = colors_int;
    }

    public static String[] getColors_string() {
        return colors_string;
    }

    public void setColors_string(String[] colors_string) {
        App.colors_string = colors_string;
    }

    public static ArrayList<String> getAllSb_str() {
        return allSb_str;
    }

    public static void setAllSb_str(ArrayList<String> allSb_str) {
        App.allSb_str = allSb_str;
    }

    public ArrayList<Subject> getUsersSubject() {
        return usersSubject;
    }

    public void setUsersSubject(ArrayList<Subject> usersSubject) {
        this.usersSubject = usersSubject;
    }

    public void setAuthController(AuthController authController) {
        App.authController = authController;
    }


    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        authController.getAllSubjectsFromDb(task -> {
            ArrayList<String> chosen_strings = new ArrayList<>();

            if (task.isSuccessful()) {
                for (DataSnapshot e : task.getResult().getChildren()) {
                    if (!allSb_str.contains(e.getValue(Subject.class).getName()))
                        allSb_str.add(e.getValue(Subject.class).getName());

                }
            }
        });



        instance = this;
    }
}
