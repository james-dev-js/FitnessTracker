package com.example.fitnesstracker.ParserAndAsync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*public class AsyncLoader extends AsyncTask<?, ?, ?>{

    private LoaderListener listener;

    public AsyncLoader(Context context){
        listener = (LoaderListener) context;
    }

    @Override
    protected void doInBackground(InputStream... inputStreams) {
        InputStream inputStream = inputStreams[0];
        ArrayList<Workout> workouts = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        Integer count = 0;
        try {
            while ((line = br.readLine()) != null) {
                String[] workout = line.split("_");
                String[] exerciseNum = workout[1].split("~");
                ArrayList<Exercise> tExercises = new ArrayList<Exercise>();
                for (int i = 1; i <= Integer.parseInt(exerciseNum[0]); i++) {
                    String[] exercises = exerciseNum[i].split("`");
                    String[] setsNum = exercises[3].split("-");
                    ArrayList<Integer[]> sets = new ArrayList<>();
                    for (int j = 1; j <= Integer.parseInt(setsNum[0]); j++) {
                        String[] setAmount = setsNum[1].split(",");
                        Integer[] setDesc = new Integer[3];
                        for (int z = 1; z < Integer.parseInt(setsNum[0]); z++) {
                            String[] set = setAmount[z].split("/");
                            setDesc[0] = Integer.parseInt(set[0]);
                            setDesc[1] = Integer.parseInt(set[1]);
                            setDesc[2] = Integer.parseInt(set[2]);
                        }
                        sets.add(setDesc);
                    }
                    tExercises.add(new Exercise(exercises[0], 0, sets, exercises[1], exercises[2]));
                }
                workouts.add(new Workout(tExercises, workout[0]));
                count++;

            }
            return workouts;
        } catch (IOException iox) {
            Log.d("doInBackground: ", iox.toString());
        }
        return workouts;
    }

    @Override
    protected void onProgressUpdate(Integer... value){
        super.onProgressUpdate(value);
        listener.giveProgress(value[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Workout> result){
        listener.onPostExecute(result);
    }
}*/
