package com.example.fitnesstracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fitnesstracker.AdaptersFolder.ExerciseAdapter;
import com.example.fitnesstracker.Database.DatabaseHelper;
import com.example.fitnesstracker.Dialogs.ExerciseAddDialog;
import com.example.fitnesstracker.Dialogs.ExerciseEditDialog;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity implements ExerciseAdapter.OnExerciseListener {

    DatabaseHelper db;
    private RecyclerView recyclerView;
    int workoutid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
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
        initRecyclerView();
    }

    //initializes the Recycler view
    private void initRecyclerView(){
    recyclerView = findViewById(R.id.exerciselist_recycler);
    ExerciseAdapter adapter = new ExerciseAdapter(this,this,workoutid);
    recyclerView.setAdapter(adapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onExerciseClick(int position) {
        ExerciseAdapter adapter = (ExerciseAdapter) recyclerView.getAdapter();
        SparseBooleanArray array = adapter.getItemArray();
        boolean completed;
        int count;
        if(recyclerView.getAdapter().getItemCount() == array.size())
        {
            count = 0;
            for(int i = 0; i < array.size();i++){
                if(array.get(i)){
                    count++;
                }
                else
                    count--;
            }
            if(count == array.size())
                Log.d("onResume: ","Selected all exercises");
                Cursor res = db.getExerciseData(workoutid);
                while(res.moveToNext()){
                    int time = (int)(Math.random() * ((60 - 45) + 1)) + 45;
                    db.insertRecordedWorkout(workoutid,res.getString(0),res.getString(4),time);
                }

        }
    }

    @Override
    public void onExerciseLongClick(int position) {

    }
}
