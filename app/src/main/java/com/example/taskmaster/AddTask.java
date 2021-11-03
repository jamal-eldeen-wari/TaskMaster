package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.List;

public class AddTask extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        EditText editText1 = findViewById(R.id.editTextTextPersonName2);
        EditText editText2 = findViewById(R.id.editTextTextPersonName4);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        DAO dao = db.dao();


        TextView textView = findViewById(R.id.textView2);

        Button btnAddTasks = findViewById(R.id.button3);

        btnAddTasks.setOnClickListener(new View.OnClickListener() {
            int count = 1;
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                textView.setText("Task Count "+count++);
                String title = editText.getText().toString();
                String description = editText1.getText().toString();
                String state = editText2.getText().toString();

                Task task = new Task(title,description,state);
                dao.insertAll(task);


            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}