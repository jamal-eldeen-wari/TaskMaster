package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.core.Amplify;

import java.io.File;

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
        ImageView imageView = findViewById(R.id.imageView2);


        textView1.setText(task1);
        textView2.setText(body);
        textView3.setText(state);

        if (intent.getExtras().getString("img")!=null){
            Amplify.Storage.downloadFile(
                    intent.getExtras().getString("img"),
                    new File(getApplicationContext().getFilesDir()+ "/" + intent.getExtras().getString("img") + ".jpg"),
                    response->{
                        Bitmap bitmap = BitmapFactory.decodeFile(response.getFile().getPath());
                        imageView.setImageBitmap(bitmap);
                        Log.i("TaskDetailsPageImage", "Successfully downloaded: " + response.getFile().getName());
                    },
                    error->{
                        Log.i("TaskDetailsPageImage", "Failed to download: " + error);
                    }
                    );
        }

        
    }
}