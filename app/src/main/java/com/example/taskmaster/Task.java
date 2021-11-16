package com.example.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey
    public Long uid;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "body")
    public String body;
    @ColumnInfo(name = "state")
    public String state;
    @ColumnInfo(name = "imageName")
    public String imageName;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }

    public Task() {
    }

    public Task(String title, String body, String state, String imageName) {
        this.title = title;
        this.body = body;
        this.state = state;
        this.imageName = imageName;
    }
}
