package com.example.fitnesstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitnesstracker.Database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    //Buttons to move from the main screen to each activity
    private Button btnTrack, btnCreate, btnProgress;
    public DatabaseHelper myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDB = new DatabaseHelper(this);

        //initializing the variable and setting on click listener
        btnTrack = findViewById(R.id.btnTrack);
        btnCreate =findViewById(R.id.btnCreate);
        btnProgress = findViewById(R.id.btnProgress);
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrackWorkout();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateWorkout();
            }
        });
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YourProgress();
            }
        });
    }

    private void TrackWorkout(){
        Intent intent = new Intent(this, TrackWorkout.class);
        startActivity(intent);
    }

    private void CreateWorkout(){
        Intent intent = new Intent(this, CreateWorkout.class);
        startActivity(intent);
    }

    private void YourProgress(){
        Intent intent = new Intent(this, Graph.class);
        startActivity(intent);
    }

}
