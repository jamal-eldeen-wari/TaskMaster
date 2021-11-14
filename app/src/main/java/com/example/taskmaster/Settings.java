package com.example.taskmaster;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

public class Settings extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sh.edit();

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.editTextTextPersonName3);
                String userName = editText.getText().toString();
                editor.putString("username",userName);
                Intent intent = new Intent(Settings.this, MainActivity.class);
                editor.commit();
                startActivity(intent);
            }
        });

        RadioGroup radioGroup = findViewById(R.id.sittingsRadioGroup);
        RadioButton radioButton1 = findViewById(R.id.team_1RBTN);
        RadioButton radioButton2 = findViewById(R.id.team_2RBTN);
        RadioButton radioButton3 = findViewById(R.id.team_3RBTN);

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
        sh.edit().putString("settingsTeamID",teamName).apply();


    }
}