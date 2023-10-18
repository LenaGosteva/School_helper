package com.example.school.Logic;


import com.example.school.App;
import com.example.school.Auth.AuthController;
import com.example.school.R;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.List;

public class Day {
    DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");

    String date  = dateFormat.format(new Date());

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    List<Subject> subjects = new ArrayList<>();
    List<Task> tasks = new ArrayList<>();

    public Day(String data) {
        this.date = data;
//        subjects.add(new Subject("Additionally", "Some additional things that are not related to the lessons", R.color.sb_purple));


    }
    public Day() {
        date = dateFormat.format(new Date());
//        subjects.add(new Subject("Additionally", "Some additional things that are not related to the lessons", R.color.sb_purple));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }
    public void addSubject(Subject subject){
        List<String> l = new ArrayList();
        for (Subject e: subjects) {
                l.add(e.getName());
        }
        if (!l.contains(subject.getName())){
            subjects.add(subject);
        }

    }public void addTask(Task task){
        List<String> l = new ArrayList();
        for (Task e: tasks) {
                l.add(e.getName());
        }
        if (!l.contains(task.getName())){
            tasks.add(task);
        }

    }
    public void add(boolean j, Subject s){

    }
    public void addSubjects(List<Subject> subject){
        subjects.addAll(subject);
    }
    public void addTasks(List<Task> task){
        task.addAll(task);
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
