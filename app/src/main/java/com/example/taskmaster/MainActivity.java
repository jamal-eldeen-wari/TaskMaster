package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.AnalyticsProperties;
import com.amplifyframework.analytics.UserProfile;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.analytics.pinpoint.models.AWSPinpointUserProfile;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main Activity";
    private static PinpointManager pinpointManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            Button btnAdd = findViewById(R.id.addTask);
        Button btnAll = findViewById(R.id.allTasks);
        TextView textView = findViewById(R.id.signOut);
        getPinpointManager(getApplicationContext());
        assignUserIdToEndpoint();

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
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            Amplify.configure(getApplicationContext());


            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }





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

        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("PasswordReset")
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", 120.3)
                .build();

        Amplify.Analytics.recordEvent(event);

        UserProfile.Location location = UserProfile.Location.builder()
                .latitude(31.992079)
                .longitude(35.845488)
                .postalCode("11118")
                .city("Amman")
                .region("Khilda")
                .country("Jordan")
                .build();

        AnalyticsProperties customProperties = AnalyticsProperties.builder()
                .add("property1", "Property value")
                .build();

        AnalyticsProperties userAttributes = AnalyticsProperties.builder()
                .add("someUserAttribute", "User attribute value")
                .build();

        AWSPinpointUserProfile profile = AWSPinpointUserProfile.builder()
                .name("Jamal Wari")
                .email("warijamalk@gmail.com")
                .plan("To Hate Errorororroororororororororororsssss")
                .location(location)
                .customProperties(customProperties)
                .userAttributes(userAttributes)
                .build();

        String userId = Amplify.Auth.getCurrentUser().getUserId();

        Amplify.Analytics.identifyUser(userId, profile);


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
        recyclerViewItem();


    }
    private void recyclerViewItem(){
        List<com.amplifyframework.datastore.generated.model.Task> tasks = new ArrayList<>();
        Log.i(TAG, "This is the tasks "+ tasks);
        RecyclerView allTasks = findViewById(R.id.taskRecyclerView);
        allTasks.setLayoutManager(new LinearLayoutManager(this));
        allTasks.setAdapter(new TaskAdapter(tasks));
        Log.i(TAG, "This is the allTasks "+ allTasks);


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
                        tasks.add(todo);

                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );
    }
    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i(TAG, "INIT => " + userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }
    public void assignUserIdToEndpoint() {
        TargetingClient targetingClient = pinpointManager.getTargetingClient();
        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
        EndpointProfileUser endpointProfileUser = new EndpointProfileUser();
        endpointProfileUser.setUserId("UserIdValue");
        endpointProfile.setUser(endpointProfileUser);
        targetingClient.updateEndpointProfile(endpointProfile);
        Log.d(TAG, "Assigned user ID " + endpointProfileUser.getUserId() +
                " to endpoint " + endpointProfile.getEndpointId());
    }

}