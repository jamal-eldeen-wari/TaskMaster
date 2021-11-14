package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main Activity";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            Button btnAdd = findViewById(R.id.addTask);
        Button btnAll = findViewById(R.id.allTasks);
        TextView textView = findViewById(R.id.signOut);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddTask.class);
                startActivity(intent);
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllTasks.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

        try {
            // Add these lines to add the AWSApiPlugin plugins
            Amplify.addPlugin(new AWSApiPlugin()); // stores things in DynamoDB and allows us to perform GraphQL queries
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());


            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }



        List<Task> tasks = new ArrayList<>();
        RecyclerView allTasks = findViewById(R.id.taskRecyclerView);
        allTasks.setLayoutManager(new LinearLayoutManager(this));
        allTasks.setAdapter(new TaskAdapter(tasks));


        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                allTasks.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task todo : response.getData()) {
                        Task taskOrg = new Task(todo.getTitle(),todo.getBody(),todo.getState());
                        Log.i("graph testing", todo.getTitle());
                        tasks.add(taskOrg);
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }

        });

//==============================LAB27==============================================
//        findViewById(R.id.task1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TaskDetailPage.class);
//                String task3 = "Task 1";
//                intent.putExtra("task3",task3);
//                startActivity(intent);
//            }
//        });
//
//        findViewById(R.id.task2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TaskDetailPage.class);
//                String task3 = "Task 2";
//                intent.putExtra("task3",task3);
//                startActivity(intent);
//            }
//        });
//
//        findViewById(R.id.task3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TaskDetailPage.class);
//                String task3 = "Task 3";
//                intent.putExtra("task3",task3);
//                startActivity(intent);
//            }
//        });

//============================LAB28===============================================
//        ArrayList<Task> taskData = new ArrayList<Task>();

//        taskData.add(new Task("Data Structures and Algorithms","Graph","new"));
//        taskData.add(new Task("Object Oriented Programming","Inheritance","assigned"));
//        taskData.add(new Task("Object Oriented Programming","Classes","complete"));

//        RecyclerView allTasks = findViewById(R.id.taskRecyclerView);
//        allTasks.setLayoutManager(new LinearLayoutManager(this));
//
//        allTasks.setAdapter(new TaskAdapter(taskData));



    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "database-name").allowMainThreadQueries().build();
//        DAO userDao = db.dao();
//        List<Task> tasks = userDao.getAll();
//        RecyclerView allTasks = findViewById(R.id.taskRecyclerView);
//        allTasks.setLayoutManager(new LinearLayoutManager(this));
//        allTasks.setAdapter(new TaskAdapter(tasks));
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name =  sharedPreferences.getString("username","Welcome User");
        String email =  sharedPreferences.getString("email","Welcome User");
        TextView textView = findViewById(R.id.userNameView);
        TextView emailText = findViewById(R.id.textView22);
        textView.setText("Welcome: "+name);
        emailText.setText("Email: "+ email);


    }
}