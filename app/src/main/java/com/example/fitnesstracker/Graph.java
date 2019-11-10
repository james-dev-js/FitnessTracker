package com.example.fitnesstracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.fitnesstracker.Database.DatabaseHelper;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Graph extends AppCompatActivity {

    DatabaseHelper db;
    Spinner spnWorkouts;
    Spinner spnExercises;
    ArrayList<String> listWorkouts = new ArrayList<>();
    ArrayList<String> listExercises;
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    GraphView graphView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        db = new DatabaseHelper(getBaseContext());
        graphView = findViewById(R.id.newGraph);
        spnWorkouts = findViewById(R.id.spnWorkout);
        spnExercises = findViewById(R.id.spnExercise);
        graphView.addSeries(series);
        //graphView.getViewport().setScalable(true);
        //graphView.getViewport().setScalableY(true);
        ActionBar actionBar = getSupportActionBar();
        if(getSupportActionBar() != null){
            actionBar.setTitle(R.string.progress);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        spnWorkouts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                preLoadGraphExercise(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnExercises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                populateGraph(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        preLoadGraph();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trackmenu, menu);
        MenuItem item = menu.findItem(R.id.menu_progress);
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
            case R.id.menu_create:
                intent = new Intent(this, CreateWorkout.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preLoadGraph(){
        Cursor res = db.getWorkoutData();
        while(res.moveToNext()){
            listWorkouts.add(res.getString(1));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listWorkouts);
        spnWorkouts.setAdapter(arrayAdapter);
    }

    private void preLoadGraphExercise(String workoutName){
        listExercises = new ArrayList<>();
        Cursor res = db.getWorkoutRow(workoutName);
        res.moveToNext();
        Cursor exc = db.getExerciseData(res.getInt(0));
        while(exc.moveToNext()){
            listExercises.add(exc.getString(1));
        }
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listExercises);
        spnExercises.setAdapter(arrayAdapter2);
    }

    private void populateGraph(String exerciseName){
        graphView.removeSeries(series);
        series = new LineGraphSeries<>();
        series.setThickness(8);
        series.setDrawDataPoints(true);
        String w_id;
        Cursor res = db.getWorkoutRow(spnWorkouts.getSelectedItem().toString());
        res.moveToNext();
        w_id = res.getString(0);
        Cursor exc = db.getExerciseData(res.getInt(0));
        String e_id;
        while(exc.moveToNext())
        {

            if(exerciseName.equals(exc.getString(1))){
                e_id = exc.getString(1);
                break;
            }
        }
        Cursor exerciseData = db.getSavedExerciseData(res.getInt(0),exc.getInt(0));
        Date x = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        while(exerciseData.moveToNext()){
            try {

                x = dateFormat.parse(exerciseData.getString(5));
            } catch (Exception e)
            {
                Log.d("populateGraph: ", e.toString());
            }
            series.appendData(new DataPoint(x,exerciseData.getInt(3)),true,);
            Log.d("populateGraph DATAPOINT:", x.toString() + " " + exerciseData.getInt(3));
        }
        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(exerciseData.getCount());
        graphView.addSeries(series);
    }

}
