package com.example.school.Logic;

import com.example.school.App;

import java.util.HashMap;

public class Subject{
    String name = "Предмет";
    String description = " ";

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    int color;
    HashMap<String, Task> tasks = new HashMap<>();

    public Subject(){
 description = "";
    }
    public void addTask(Task task){
        tasks.put(task.getName(), task);
        App.authController.addTaskToSubject(task, task.getName(), name, cl ->{});
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

    public  HashMap<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks( HashMap<String, Task> tasks) {
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
