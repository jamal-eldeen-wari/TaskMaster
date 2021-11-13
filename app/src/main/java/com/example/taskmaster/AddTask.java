package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.List;

public class AddTask extends AppCompatActivity {
    public static final String TAG = "ADD TASK";

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
        String[] teams = new String[]{"Team 1", "Team 2", "Team 3"};
        RadioGroup radioGroup = findViewById(R.id.radioGroupAddTask);
        RadioButton radioButton1 = findViewById(R.id.team_1RBtn);
        RadioButton radioButton2 = findViewById(R.id.team_2RBtn);
        RadioButton radioButton3 = findViewById(R.id.team_3RBtn);
        String teamName;

        int getCheckButton = radioGroup.getCheckedRadioButtonId();
        if (getCheckButton == radioButton1.getId()){

            teamName = radioButton1.getText().toString();

        }else if(getCheckButton == radioButton2.getId()){

            teamName = radioButton2.getText().toString();

        }else if(getCheckButton == radioButton3.getId()){

            teamName = radioButton3.getText().toString();

        }else {
            teamName = null;
        }



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
                Team team = Team.builder().teamName("Gamers").build();
                Task task = Task.builder()
                        .title(title)
                        .body(description)
                        .state(state).team(team)
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