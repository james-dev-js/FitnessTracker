package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fitnesstracker.AdaptersFolder.WorkoutAdapter;
import com.example.fitnesstracker.Database.DatabaseHelper;

public class TrackWorkout extends AppCompatActivity implements WorkoutAdapter.OnWorkoutListener/*, LoaderListener*/ {

    //List of Stored workouts
    DatabaseHelper db;

    //using an async loader to load the workouts in
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_workout);
        db = new DatabaseHelper(getBaseContext());

        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null){
            actionBar.setTitle(R.string.track);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initRecyclerView();
    }

    //implements the menu where you can switch to another activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trackmenu, menu);
        MenuItem item = menu.findItem(R.id.menu_track);
        item.setVisible(false);
        return true;
    }

    //Handles user selection of the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_main:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            case R.id.menu_create:
                intent = new Intent(this, CreateWorkout.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            case R.id.menu_progress:
                intent = new Intent(this, Graph.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

   ///initializes the Recycler view
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.workout_recycler);
        WorkoutAdapter adapter = new WorkoutAdapter(this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Implementing the RecyclerView onClick
    @Override
    public void onWorkoutClick(int position) {
        //Creates new intent
        Intent intent = new Intent(this, WorkoutActivity.class);
        //Creates a new Bundle and adds the current workout select to put in the bundle then intent
        Bundle myBundle = new Bundle();
        myBundle.putInt("row",position);
        intent.putExtras(myBundle);
        //Starts new Activity
        startActivity(intent);
    }

    @Override
    public void onWorkoutLongClick(int position) {

    }
}
