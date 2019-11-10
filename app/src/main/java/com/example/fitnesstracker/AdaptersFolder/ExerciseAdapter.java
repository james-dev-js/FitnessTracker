package com.example.fitnesstracker.AdaptersFolder;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.Database.DatabaseHelper;
import com.example.fitnesstracker.Exercise;
import com.example.fitnesstracker.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ViewHolder>{

    //Holds all the names of the user created workouts
    private int mWorkoutid;
    private ArrayList<Exercise> items = new ArrayList<>();
    SparseBooleanArray itemArray = new SparseBooleanArray();
    private Context mContext;
    private DatabaseHelper mDb;
    private OnExerciseListener mOnExerciseListener;

    //Constructor
    public ExerciseAdapter(Context context, OnExerciseListener onExerciseListener,int workoutid){
        this.mWorkoutid = workoutid;
        this.mDb = new DatabaseHelper(context);
        mContext = context;
        this.mOnExerciseListener = onExerciseListener;
    }

    public SparseBooleanArray getItemArray() {
        return itemArray;
    }

    //Attaching the Track list item to the RecylerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_exercise_item, parent, false);
        ViewHolder holder = new ViewHolder(view,mOnExerciseListener);
        return holder;
    }

    //setting the title of the workout from the db
    @Override
    public void onBindViewHolder(ViewHolder holder, int pos){
        Cursor res = mDb.getExerciseRoutineData(mWorkoutid);
        Cursor exc;
        if(res.getCount() == 0){
            Toast.makeText(mContext, "NO DATA IN TABLE", Toast.LENGTH_LONG).show();
            return;
        }
        res.moveToNext();

        exc = mDb.getExerciseData(mWorkoutid);
        exc.moveToPosition(pos);
        if(exc.getString(1) != null) {
            String Reps = exc.getString(4) + " reps";
            String KG = exc.getString(5) + " kg";
            holder.mExerciseName.setText(exc.getString(1));
            holder.mExerciseReps.setText(Reps);
            holder.mExerciseKg.setText(KG);
        }
        holder.setChecked(pos);
    }

    @Override
    public int getItemCount(){
        Cursor res = mDb.getExerciseData(mWorkoutid);
        return res.getCount();
    }

    //Override the view holder for a custom view holder so we can load anything in
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mExerciseName,mExerciseReps,mExerciseKg;
        private RadioButton mDone;
        private RelativeLayout parentLayout;
        OnExerciseListener onExerciseListener;

        public ViewHolder(View view, OnExerciseListener pOnExerciseListener){
            super(view);
            mExerciseName = view.findViewById(R.id.exerciseName);
            mExerciseReps = view.findViewById(R.id.exerciseReps);
            mExerciseKg = view.findViewById(R.id.exerciseKg);
            mDone = view.findViewById(R.id.btnDone);
            parentLayout = view.findViewById(R.id.parent_track);
            mDone.setOnClickListener(this);
            this.onExerciseListener = pOnExerciseListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }
        private void setChecked(int pos){
            if(!itemArray.get(pos,false))
                mDone.setChecked(false);
            else
                mDone.setChecked(true);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            if(!itemArray.get(adapterPos,false)){
                mDone.setChecked(true);
                itemArray.put(adapterPos,true);
            }
            else {
                mDone.setChecked(false);
                itemArray.put(adapterPos, false);
            }
            onExerciseListener.onExerciseClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            onExerciseListener.onExerciseLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnExerciseListener{
        void onExerciseClick(int position);
        void onExerciseLongClick(int position);
    }
}
