package com.example.fitnesstracker;

import com.example.fitnesstracker.AdaptersFolder.WorkoutAdapter;
import com.example.fitnesstracker.Database.DatabaseHelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fitnesstracker.Dialogs.WorkoutAddDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CreateWorkout extends AppCompatActivity implements WorkoutAddDialog.WorkoutAddDialogListener, WorkoutAdapter.OnWorkoutListener {

    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        db = new DatabaseHelper(getBaseContext());
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null){
            actionBar.setTitle(R.string.create);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton add = findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        //initializes the Recycler view
        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.workout_recycler);
        WorkoutAdapter adapter = new WorkoutAdapter(this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //implements the menu where you can switch to another activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trackmenu, menu);
        MenuItem item = menu.findItem(R.id.menu_create);
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
            case R.id.menu_track:
                intent = new Intent(this, TrackWorkout.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            case R.id.menu_settings:
                viewWorkouts();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openDialog(){
        WorkoutAddDialog addDialog = new WorkoutAddDialog();
        addDialog.show(getSupportFragmentManager(), "workout add dialog");
    }

    @Override
    public void applyText(String workoutName) {
        boolean isInserted = db.insertWorkoutData(workoutName);

        if(isInserted) {
            Toast.makeText(CreateWorkout.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(CreateWorkout.this,"Data didn't insert", Toast.LENGTH_LONG).show();
        initRecyclerView();
    }
    //Implementing the RecyclerView onClick
    @Override
    public void onWorkoutClick(int position) {
        //Creates new intent
        Intent intent = new Intent(this, WorkoutEditActivity.class);
        //Creates a new Bundle and adds the current workout select to put in the bundle then intent
        Bundle myBundle = new Bundle();
        myBundle.putInt("row", position);
        intent.putExtras(myBundle);
        //Starts new Activity
        startActivity(intent);
    }

    @Override
    public void onWorkoutLongClick(int position) {
        /*if(res.getCount() == 0){
            Toast.makeText(this, "NO DATA IN TABLE", Toast.LENGTH_LONG).show();
            return;
        }*/
        Log.d("onExerciseLongClick: ", Integer.toString(position));
        db.dropWorkout(position);
        initRecyclerView();
    }

    //Developer Functions
    public void viewWorkouts(){
        Cursor res = db.getWorkoutData();
        if(res.getCount() == 0){
            showMessage("ERROR", "NOTHING FOUND");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append("ID :" + res.getString(0) + "\n");
            buffer.append("Workout Name :" + res.getString(1) + "\n\n");
        }

        showMessage("DATA", buffer.toString());

        //show the data
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
