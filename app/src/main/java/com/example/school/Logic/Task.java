package com.example.school.Logic;

public class Task {
    String name = "Имя задания";
    String comment = "Комментарий задания";
    String theory = "Теория задания";
    String practice = "Практика задания";
    String subject = "Дополнительно задания";
    int panic;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    boolean isCompleted = false;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    public String getPractice() {
        return practice;
    }

    public void setPractice(String practice) {
        this.practice = practice;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Task(String name, String comment, String theory, String practice, String subject) {
        this.name = name;
        this.comment = comment;
        this.theory = theory;
        this.practice = practice;
        this.subject = subject;
        this.panic = 1;
    }

    public Task(String name, String comment, String theory, String practice, String subject, int panic, boolean isCompleted) {
        this.name = name;
        this.comment = comment;
        this.theory = theory;
        this.practice = practice;
        this.subject = subject;
        this.panic = panic;
        this.isCompleted = isCompleted;
    }

    public int getPanic() {
        return panic;
    }

    public void setPanic(int panic) {
        this.panic = panic;
    }

    public Task(){
    }

}
