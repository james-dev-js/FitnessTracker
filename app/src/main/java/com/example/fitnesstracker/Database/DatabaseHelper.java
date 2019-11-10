package com.example.fitnesstracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    //Workout Reference Table
    public static final String TABLE_WORKOUT = "workouts";
    public static final String COLUMN_WORKOUT_ID = "_id";
    public static final String COLUMN_WORKOUT_NAME = "workout_name";

    //Exercise Table
    public static final String TABLE_EXERCISES = "exercises";
    public static final String COLUMN_EXERCISE_ID = COLUMN_WORKOUT_ID;
    public static final String COLUMN_EXERCISE_NAME = "exercise_name";
    public static final String COLUMN_EXERCISE_DESC = "exercise_description";
    public static final String COLUMN_EXERCISE_URL = "exercise_url";
    public static final String COLUMN_EXERCISE_REPS = "reps";
    public static final String COLUMN_EXERCISE_KG = "kilograms";

    //Workout Routines
    public static final String TABLE_WORKOUT_ROUURINES = "workouts_routines";
    public static final String COLUMN_WORKOUT_ROUURINES_ID = COLUMN_WORKOUT_ID;
    public static final String COLUMN_WORKOUT_ROUURINES_WORKOUT_ID = "workout_id";
    public static final String COLUMN_WORKOUT_ROUURINES_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_WORKOUT_ROUURINES_REPS_EXECUTED = "reps_executed";
    public static final String COLUMN_WORKOUT_ROUURINES_TIME_TAKEN = "time_taken";
    public static final String COLUMN_WORKOUT_ROUURINES_DATE = "routine_date";

    //Saved workout
    public static final String TABLE_SAVED_WORKOUTS = "saved_workouts";
    public static final String COLUMN_SAVED_WORKOUTS_ID = COLUMN_WORKOUT_ID;
    public static final String COLUMN_SAVED_WORKOUTS_WORKOUT_ID = COLUMN_WORKOUT_ROUURINES_WORKOUT_ID;
    public static final String COLUMN_SAVED_WORKOUTS_EXERCISE_ID = COLUMN_WORKOUT_ROUURINES_EXERCISE_ID;

    public static final String DATABASE_NAME = "workouts.db";
    public static final int DATABASE_VERSION = 7;

    public static final String SQL_CREATE_TABLE_EXERCISES = "CREATE TABLE " + TABLE_EXERCISES + "("
            + COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_EXERCISE_NAME + " TEXT NOT NULL, "
            + COLUMN_EXERCISE_DESC + " TEXT NOT NULL, "
            + COLUMN_EXERCISE_URL + " TEXT NOT NULL, "
            + COLUMN_EXERCISE_REPS + " TEXT NOT NULL, "
            + COLUMN_EXERCISE_KG + " TEXT NOT NULL "
            + ");";

    public static final String SQL_CREATE_TABLE_WORKOUTS = "CREATE TABLE " + TABLE_WORKOUT + "("
            + COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_WORKOUT_NAME + " TEXT NOT NULL"
            + ");";

    public static final String SQL_CREATE_TABLE_WORKOUT_ROUTINES = "CREATE TABLE " + TABLE_WORKOUT_ROUURINES+ "("
            + COLUMN_WORKOUT_ROUURINES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_WORKOUT_ROUURINES_WORKOUT_ID + " INTEGER NOT NULL, "
            + COLUMN_WORKOUT_ROUURINES_EXERCISE_ID + " INTEGER NOT NULL, "
            + COLUMN_WORKOUT_ROUURINES_REPS_EXECUTED + " INTEGER NOT NULL, "
            + COLUMN_WORKOUT_ROUURINES_TIME_TAKEN + " INTEGER NOT NULL, "
            + COLUMN_WORKOUT_ROUURINES_DATE + " INTEGER NOT NULL, "
            + " FOREIGN KEY (" + COLUMN_WORKOUT_ROUURINES_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUT + "(" + COLUMN_WORKOUT_ID + "), "
            + " FOREIGN KEY (" + COLUMN_WORKOUT_ROUURINES_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + ") "
            + ");";

    public static final String SQL_CREATE_TABLE_SAVED_WORKOUTS = "CREATE TABLE " + TABLE_SAVED_WORKOUTS+ "("
            + COLUMN_SAVED_WORKOUTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SAVED_WORKOUTS_WORKOUT_ID + " INTEGER NOT NULL, "
            + COLUMN_SAVED_WORKOUTS_EXERCISE_ID + " INTEGER NOT NULL, "
            + " FOREIGN KEY (" + COLUMN_SAVED_WORKOUTS_WORKOUT_ID + ") REFERENCES " + TABLE_WORKOUT + "(" + COLUMN_WORKOUT_ID + "), "
            + " FOREIGN KEY (" + COLUMN_SAVED_WORKOUTS_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + ") "
            + ");";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("PRAGMA foreign_keys = ON;");
        database.execSQL(SQL_CREATE_TABLE_WORKOUTS);
        database.execSQL(SQL_CREATE_TABLE_EXERCISES);
        database.execSQL(SQL_CREATE_TABLE_WORKOUT_ROUTINES);
        database.execSQL(SQL_CREATE_TABLE_SAVED_WORKOUTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        Log.d(TAG, "onUpgrade: Upgrading version" + i + " to " + i1);

        //clear all data
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_ROUURINES);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_WORKOUTS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);

        onCreate(database);

    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    //ADDING DATA TO THE DATABASE

    public boolean insertWorkoutData(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WORKOUT_NAME,name);
        long result = db.insert(TABLE_WORKOUT,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertRecordedWorkout(int w_id, String e_id, String reps, int timetaken){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WORKOUT_ROUURINES_WORKOUT_ID, w_id);
        contentValues.put(COLUMN_WORKOUT_ROUURINES_EXERCISE_ID, e_id);
        contentValues.put(COLUMN_WORKOUT_ROUURINES_REPS_EXECUTED,reps);
        contentValues.put(COLUMN_WORKOUT_ROUURINES_TIME_TAKEN, timetaken);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        contentValues.put(COLUMN_WORKOUT_ROUURINES_DATE, df.format(Calendar.getInstance().getTime()));
        long result = db.insert(TABLE_WORKOUT_ROUURINES,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertExerciseData(String name, String desc, String url, String reps, String kg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EXERCISE_NAME,name);
        contentValues.put(COLUMN_EXERCISE_DESC,desc);
        contentValues.put(COLUMN_EXERCISE_URL,url);
        contentValues.put(COLUMN_EXERCISE_REPS,reps);
        contentValues.put(COLUMN_EXERCISE_KG,kg);
        long result = db.insert(TABLE_EXERCISES,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertExerciseRoutineData(int w_id, int e_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SAVED_WORKOUTS_WORKOUT_ID,w_id);
        contentValues.put(COLUMN_SAVED_WORKOUTS_EXERCISE_ID,e_id);
        long result = db.insert(TABLE_SAVED_WORKOUTS,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //SELECTING AND PULLING INFOMATION FROM THE DATABSE
    public Cursor getWorkoutData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORKOUT,null);
        return res;
    }

    public Cursor getWorkoutRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORKOUT + " WHERE " + COLUMN_WORKOUT_ID + " = "  + id + ";",null);
        return res;
    }
    public Cursor getWorkoutRow(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if(name.contains("'"))
            name = name.replace("'","''");
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_WORKOUT + " WHERE " + COLUMN_WORKOUT_NAME + " = " + "'" + name + "'"+ ";",null);
        return res;
    }

    public Cursor getExerciseRow(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_ID + " = "  + id + ";",null);
        return res;
    }

    public Cursor getExerciseRow(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if(name.contains("'"))
            name = name.replace("'","''");
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_NAME + " = "  + "'" + name + "'" + ";",null);
        return res;
    }

    public Cursor getExerciseData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM 'exercises' LEFT JOIN 'saved_workouts' ON exercises._id = saved_workouts.exercise_id WHERE workout_id = " + id + ";"/*"SELECT * FROM " + TABLE_EXERCISES + " INNER JOIN " + TABLE_WORKOUT_ROUURINES  + " ON " + TABLE_EXERCISES + "." +COLUMN_EXERCISE_ID + " = " +  TABLE_EXERCISES+ "." +COLUMN_EXERCISE_ID+ " WHERE " + COLUMN_WORKOUT_ROUURINES_WORKOUT_ID + " = "  + id + ";"*/,null);
        return res;
    }

    public Cursor getSavedExerciseData(int w_id, int e_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM 'workouts_routines' INNER JOIN 'exercises' ON workouts_routines.exercise_id = exercises._id WHERE workout_id = "+ w_id +" AND exercise_id = "+ e_id +";",null);
        return res;
    }

    public Cursor getExerciseRoutineData(int workoutid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_SAVED_WORKOUTS + " WHERE " + COLUMN_SAVED_WORKOUTS_WORKOUT_ID + " = "  + workoutid + ";",null);
        return res;
    }

    //UPDATING VALUES
    public boolean updateExerciseRow(String id, String name, String desc, String url, String reps, String kg){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EXERCISE_ID,id);
        contentValues.put(COLUMN_EXERCISE_NAME,name);
        contentValues.put(COLUMN_EXERCISE_DESC,desc);
        contentValues.put(COLUMN_EXERCISE_URL,url);
        contentValues.put(COLUMN_EXERCISE_REPS,reps);
        contentValues.put(COLUMN_EXERCISE_KG,kg);
        long result = db.update(TABLE_EXERCISES,contentValues,  COLUMN_EXERCISE_ID + " = ? ",new String[] {id});
        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean updateWorkoutRow(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WORKOUT_ID,id);
        contentValues.put(COLUMN_WORKOUT_NAME,name);
        long result = db.update(TABLE_WORKOUT,contentValues,  COLUMN_WORKOUT_ID + " = ? ",new String[] {id});
        if(result == -1)
            return false;
        else
            return true;
    }

    //DELETING DATA
    public void dropExercise(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if(name.contains("'"))
            name = name.replace("'","''");
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_NAME + " = "  + "'" + name+ "'" + ";",null);
        res.moveToNext();
        db.execSQL("DELETE FROM " + TABLE_WORKOUT_ROUURINES + " WHERE " + COLUMN_WORKOUT_ROUURINES_EXERCISE_ID + " = " + res.getString(0) +";");
        db.execSQL("DELETE FROM " + TABLE_SAVED_WORKOUTS + " WHERE " + COLUMN_SAVED_WORKOUTS_EXERCISE_ID + " = " + res.getString(0) +";");
        db.execSQL("DELETE FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_EXERCISE_NAME + " = "+ "'" + name + "'" +";");
    }

    public void dropWorkout(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_WORKOUT_ROUURINES + " WHERE " + COLUMN_WORKOUT_ROUURINES_WORKOUT_ID + " = " + id +";");
        db.execSQL("DELETE FROM " + TABLE_SAVED_WORKOUTS + " WHERE " + COLUMN_SAVED_WORKOUTS_WORKOUT_ID + " = " + id +";");
        db.execSQL("DELETE FROM " + TABLE_WORKOUT + " WHERE " + COLUMN_WORKOUT_ID + " = " + id +";");
    }
}
