package com.example.taskmaster;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class AddTask extends AppCompatActivity {
    public static final String TAG = "ADD TASK";
    public String imageName = "";
    public Uri uri;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        EditText editText = findViewById(R.id.editTextTextPersonName);
        EditText editText1 = findViewById(R.id.editTextTextPersonName2);
        EditText editText2 = findViewById(R.id.editTextTextPersonName4);
        Button saveS3 = findViewById(R.id.saveToS3);
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

//                Task task = new Task(title,description,state);
//                dao.insertAll(task);
                Task task = Task.builder()
                        .title(title)
                        .body(description)
                        .state(state)
                        .imageName(imageName)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(task),
                        response -> Log.i(TAG, "Added task with id: " + response.getData().getId()),
                        error -> Log.e(TAG, "Create failed", error)
                );
                Intent goToHomePage = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHomePage);


            }
        });

        saveS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInputStream();
//                ActivityResultLauncher<Intent> someActivityResultLaunch = registerForActivityResult(
//                        new ActivityResultContracts.StartActivityForResult(),
//                        result -> {
//                            if (result.getResultCode() == Activity.RESULT_OK){
//                                Intent intent = result.getData();
//                            }
//                        }
//                );

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent = Intent.createChooser(intent,"intent");
                startActivityForResult(intent,44);
            }
        });
//        ActivityResultLauncher<Intent> someActivityResultLaunch = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Intent intent = result.getData();
//
//                        }
//                    }
//                } {
//                }
//        )
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
//    private void uploadFile() {
//        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");
//
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
//            writer.append("I HATE ERRORRRORORORORORROROROS");
//            writer.close();
//        } catch (Exception exception) {
//            Log.e("MyAmplifyApp", "Upload failed", exception);
//        }
//
//        Amplify.Storage.uploadFile(
//                "ExampleKey",
//                exampleFile,
//                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
//                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
//        );
//    }

    private void uploadInputStream() {
        if (uri!= null) {


            try {
                InputStream exampleInputStream = getContentResolver().openInputStream(uri);

                Amplify.Storage.uploadInputStream(
                        imageName,
                        exampleInputStream,
                        result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                );
            } catch (FileNotFoundException error) {
                Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        File file = new File(data.getData().getPath());
//        imageName = file.getName();
//        uri = data.getData();

        File uploadFile = new File(getApplicationContext().getFilesDir(), "uploadFileCopied");
        try {
            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());
            OutputStream outputStream = new FileOutputStream(uploadFile);
            imageName = data.getData().toString();
            byte[] buff = new byte[1024];
            int length;
            while ((length = exampleInputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            exampleInputStream.close();
            outputStream.close();
            Amplify.Storage.uploadFile(
                    "image",
                    uploadFile,
                    result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}