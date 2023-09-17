package com.example.school.Logic;

import com.example.school.App;

import java.util.ArrayList;

public class Subject {
    String name = "Предмет";
    String description = " ";

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color;
    ArrayList<Task> tasks = new ArrayList<Task>();

    public Subject(){
 description = "";
    }
    public void addTask(Task task){
        tasks.add(task);
        App.authController.addTaskToSubject(task, String.valueOf(tasks.indexOf(task)), name, cl ->{});
    }
    public void getTask(int index){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Subject(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public Subject(String name, String description, int color) {
        this.name = name;
        this.color = color;
        this.description = description;
    }
    public String  toString(){
        return name + "\n"+color+"\n"+ description;
    }



}
