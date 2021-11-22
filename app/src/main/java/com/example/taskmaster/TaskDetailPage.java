package com.example.taskmaster;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

public class TaskDetailPage extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;

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

        String url = intent.getExtras().getString("img");
        ImageView image = findViewById(R.id.imageView2);
        Picasso.get().load(url).into(image);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        this.googleMap = googleMap;
        Intent intent = getIntent();
        LatLng myLocation = new LatLng(getIntent().getDoubleExtra("lat", intent.getFloatExtra("lat",0)),
                getIntent().getDoubleExtra("lon", intent.getFloatExtra("lon",0)));
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("My Location In Jordan"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }
}