package com.example.reditask;

public class Task {
    private String task_title, task_desc, task_date, task_time;

    public Task(String task_title, String task_desc, String task_date, String task_time) {
        this.task_title = task_title;
        this.task_desc = task_desc;
        this.task_date = task_date;
        this.task_time = task_time;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_desc() {
        return task_desc;
    }

    public void setTask_desc(String task_desc) {
        this.task_desc = task_desc;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public String getTask_time() {
        return task_time;
    }

    public void setTask_time(String task_time) {
        this.task_time = task_time;
    }
}
