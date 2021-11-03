package com.example.taskmaster;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface DAO {
    @Query("SELECT * FROM task")
    List<Task> getAll();

    @Query("SELECT * FROM task WHERE uid =:id")
    List<Task> loadAllByIds(Long[] id);

    @Insert
    void insertAll(Task... tasks);
}
