package com.example.taskmaster;

public class Task {
    public String title;
    public String body;
    public String state;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public Task() {
    }
}
