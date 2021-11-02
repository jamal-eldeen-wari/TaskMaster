package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class TaskDetailPage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(TaskDetailPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent  = getIntent();
        String task1 = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        String state = intent.getStringExtra("state");
        TextView textView1 = findViewById(R.id.titleTask);
        TextView textView2 = findViewById(R.id.bodyTask);
        TextView textView3 = findViewById(R.id.stateTask);

        textView1.setText(task1);
        textView2.setText(body);
        textView3.setText(state);


//        Intent intentTask1 = getIntent();
//        String task1 = intentTask1.getExtras().getString("task1");
//        TextView textView1 = findViewById(R.id.textView14);
//        textView1.setText(task1);
//
//        Intent intentTask2 = getIntent();
//        String task2 = intentTask2.getExtras().getString("task2");
//        @SuppressLint("CutPasteId") TextView textView2 = findViewById(R.id.textView14);
//        textView2.setText(task2);
//
//        Intent intentTask3 = getIntent();
//        String task3 = intentTask3.getExtras().getString("task3");
//        @SuppressLint("CutPasteId") TextView textView3 = findViewById(R.id.textView14);
//        textView3.setText(task3);
    }
}