package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.room.Room;

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
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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


//=============================== 3 TEAMS Manually As Requested=================================================================

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        List<Team> allTeams  = new ArrayList<>();

        allTeams.add(Team.builder().teamName("Team 1").build());
        allTeams.add(Team.builder().teamName("Team 2").build());
        allTeams.add(Team.builder().teamName("Team 3").build());

        Amplify.API.mutate(
                ModelMutation.create(allTeams.get(0)),success->{
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "Team 1 id is:  " + success.getData().getId());
                        sh.edit().putString(allTeams.get(0).getTeamName(), success.getData().getId()).apply();
                    }

        },error->{
                   Log.i(TAG,"I hate Errorssssssssssssss");
                });

        Amplify.API.mutate(
                ModelMutation.create(allTeams.get(1)),success->{
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "Team 2 id is: " + success.getData().getId());
                        sh.edit().putString(allTeams.get(1).getTeamName(), success.getData().getId()).apply();
                    }
                },error->{

                    Log.i(TAG,"I hate Errorsssssss");
                }
        );
        Amplify.API.mutate(
                ModelMutation.create(allTeams.get(2)),success->{
                    if (success.getData().getId() != null) {
                        Log.i(TAG, "Team 3 id is: " + success.getData().getId());
                        sh.edit().putString(allTeams.get(2).getTeamName(), success.getData().getId()).apply();
                    }
                },error->{

                    Log.i(TAG,"I hate Errorsssssss");
                }
        );

//===================================Getting Tasks and view them in the recycler view========================================

        List<Task> tasks1 = new ArrayList<>();
        List<com.amplifyframework.datastore.generated.model.Task> taskList = new ArrayList<>();

        String teamIdFromSettings = sh.getString("settingsTeamID",null);

        if(teamIdFromSettings == null){


        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),success->{

                    for(com.amplifyframework.datastore.generated.model.Task task:success.getData()){
                        taskList.add(task);
                    }

                    Collections.sort(taskList, new Comparator<com.amplifyframework.datastore.generated.model.Task>() {
                        @Override
                        public int compare(com.amplifyframework.datastore.generated.model.Task o1, com.amplifyframework.datastore.generated.model.Task o2) {
                            return Long.compare(o1.getCreatedAt().toDate().getTime(), o2.getCreatedAt().toDate().getTime());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                    for (com.amplifyframework.datastore.generated.model.Task task: taskList){
                        tasks1.add(new Task(task.getTitle(),task.getBody(),task.getState()));
                    }
                    handler.sendEmptyMessage(1);
                },error->{
                    Log.i(TAG,"Errrorrrs");

                }
        );
        }else{
            Amplify.API.query(
                    ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class,
                            com.amplifyframework.datastore.generated.model.Task.TEAM.contains(teamIdFromSettings)),
                    success->{
                        for (com.amplifyframework.datastore.generated.model.Task task:success.getData()){
                            taskList.add(task);
                        }
                        Collections.sort(taskList, new Comparator<com.amplifyframework.datastore.generated.model.Task>() {
                            @Override
                            public int compare(com.amplifyframework.datastore.generated.model.Task o1, com.amplifyframework.datastore.generated.model.Task o2) {
                                return Long.compare(o1.getCreatedAt().toDate().getTime(), o2.getCreatedAt().toDate().getTime());
                            }

                            @Override
                            public boolean equals(Object obj) {
                                return false;
                            }
                        });
                        for (com.amplifyframework.datastore.generated.model.Task task: taskList){
                            tasks1.add(new Task(task.getTitle(),task.getBody(),task.getState()));
                        }
                        handler.sendEmptyMessage(1);


                    },error->{

                    }
            );
        }




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
        TextView textView = findViewById(R.id.userNameView);
        textView.setText("Welcome: "+name);


    }


}