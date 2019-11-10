package com.example.fitnesstracker;

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

import com.example.fitnesstracker.AdaptersFolder.ExerciseAdapter;
import com.example.fitnesstracker.AdaptersFolder.WorkoutAdapter;
import com.example.fitnesstracker.Database.DatabaseHelper;
import com.example.fitnesstracker.Dialogs.ExerciseAddDialog;
import com.example.fitnesstracker.Dialogs.ExerciseEditDialog;
import com.example.fitnesstracker.Dialogs.WorkoutAddDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WorkoutEditActivity extends AppCompatActivity implements ExerciseAddDialog.ExerciseAddDialogListener, ExerciseAdapter.OnExerciseListener, ExerciseEditDialog.ExerciseEditDialogListener {

    DatabaseHelper db;
    int workoutid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);
        db = new DatabaseHelper(getBaseContext());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Cursor res = db.getWorkoutRow(bundle.getInt("row"));
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null){
            while(res.moveToNext()){
                workoutid = res.getInt(0);
                actionBar.setTitle(res.getString(1));
                break;
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton add = findViewById(R.id.addExercise);
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
        RecyclerView recyclerView = findViewById(R.id.rcEditExercise);
        ExerciseAdapter adapter = new ExerciseAdapter(this,this, workoutid);
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
            case R.id.menu_progress:
                intent = new Intent(this, Graph.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            case R.id.menu_settings:
                viewExercises();
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    ///edit
    public void openDialog(){
        ExerciseAddDialog addDialog = new ExerciseAddDialog();
        addDialog.show(getSupportFragmentManager(), "exercise add dialog");
    }

    public void viewExercises(){
        Cursor res = db.getExerciseRoutineData(workoutid);
        Cursor exc;
        if(res.getCount() == 0){
            showMessage("ERROR", "NOTHING FOUND");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        StringBuffer bufferExercise = new StringBuffer();
        while(res.moveToNext()){

            exc = db.getExerciseRow(res.getInt(2));
            Log.d("viewExercises: ", res.getString(2));
            while(exc.moveToNext())
            {
                bufferExercise.append("ID :" + exc.getString(0) + "\n");
                bufferExercise.append("Exercise Name :" + exc.getString(1) + "\n");
                bufferExercise.append("Desc :" + exc.getString(2) + "\n");
                bufferExercise.append("URL :" + exc.getString(3) + "\n");
                bufferExercise.append("Reps :" + exc.getString(4) + "\n");
                bufferExercise.append("KG :" + exc.getString(5) + "\n\n");
                break;
            }
            buffer.append(bufferExercise);
        }

        showMessage("Exercises", buffer.toString());

        //show the data
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void applyText(String exerciseName, String exerciseDesc, String exerciseURL, String exerciseReps, String exerciseKg) {
        boolean isInserted = db.insertExerciseData(exerciseName,exerciseDesc,exerciseURL,exerciseReps,exerciseKg);
        boolean isInsertedTwo = false;
        Cursor res = db.getExerciseRow(exerciseName);
        while(res.moveToNext()){
            isInsertedTwo= db.insertExerciseRoutineData(workoutid,res.getInt(0));
            break;
        }

        if(isInserted && isInsertedTwo) {
            Toast.makeText(WorkoutEditActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(WorkoutEditActivity.this,"Data didn't insert", Toast.LENGTH_LONG).show();
        initRecyclerView();
    }

    @Override
    public void ediText(int id,String exerciseName, String exerciseDesc, String exerciseURL, String exerciseReps, String exerciseKg) {
        boolean isUpdated = db.updateExerciseRow(Integer.toString(id),exerciseName,exerciseDesc,exerciseURL,exerciseReps,exerciseKg);

        if(isUpdated) {
            Toast.makeText(WorkoutEditActivity.this, "Data Updated", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(WorkoutEditActivity.this,"Data didn't update", Toast.LENGTH_LONG).show();
        initRecyclerView();
    }

    @Override
    public void onExerciseClick(int position) {
        Cursor exc = db.getExerciseData(workoutid);
        exc.moveToPosition(position);
        ExerciseEditDialog exerciseEditDialog = new ExerciseEditDialog(exc.getInt(0),this);
        exerciseEditDialog.show(getSupportFragmentManager(), "exercise edit dialog");
    }

    @Override
    public void onExerciseLongClick(int position) {
        Cursor res = db.getExerciseRoutineData(workoutid);
        Cursor exc;
        if(res.getCount() == 0){
            Toast.makeText(this, "NO DATA IN TABLE", Toast.LENGTH_LONG).show();
            return;
        }
        res.moveToNext();

        exc = db.getExerciseData(workoutid);
        exc.moveToPosition(position);
        if(exc.getString(1) != null)
            db.dropExercise(exc.getString(1));
            //holder.mExerciseName.setText(exc.getString(1));

        initRecyclerView();
    }
}
