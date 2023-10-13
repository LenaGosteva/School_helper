package com.example.school;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.example.school.Auth.AuthController;
import com.example.school.Logic.Subject;
import com.google.firebase.database.DataSnapshot;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
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
    static int[] colors_int_fill =
            {R.color.sb_red_fill,
                    R.color.sb_orange_fill,
                    R.color.sb_yellow_fill,
                    R.color.sb_green_fill,
                    R.color.sb_blue_fill,
                    R.color.sb_purple_fill,
                    R.color.sb_brown_fill};
    static int[] colors_int_dark =
            {R.color.sb_red_dark,
                    R.color.sb_orange_dark,
                    R.color.sb_yellow_dark,
                    R.color.sb_green_dark,
                    R.color.sb_blue_dark,
                    R.color.sb_purple_dark,
                    R.color.sb_brown_dark};
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

    public static int[] getColors_int_fill() {
        return colors_int_fill;
    }

    public static void setColors_int_fill(int[] colors_int_fill) {
        App.colors_int_fill = colors_int_fill;
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

    public static int[] getColors_int_dark() {
        return colors_int_dark;
    }

    public static void setColors_int_dark(int[] colors_int_dark) {
        App.colors_int_dark = colors_int_dark;
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
    public static boolean isConnectedToNetwork() {
//        try {
//            URL url = new URL("https://www.google.com");
//            URLConnection connection = url.openConnection();
//            connection.connect();
//            Log.e("75e7", "Connection Successful");
//            return true;
//        } catch (Exception e) {
//            Log.e("75e7", "Connection Not Successful\n"+ e);
//            return false;
//        }
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return true;
        }
    }
}
