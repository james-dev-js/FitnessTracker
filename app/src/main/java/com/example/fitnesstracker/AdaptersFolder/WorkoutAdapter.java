package com.example.fitnesstracker.AdaptersFolder;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnesstracker.Database.DatabaseHelper;
import com.example.fitnesstracker.R;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private DatabaseHelper mDb;
    private OnWorkoutListener mOnWorkoutListener;

    public WorkoutAdapter(Context context, OnWorkoutListener onWorkoutListener){
        mDb = new DatabaseHelper(context);
        mOnWorkoutListener = onWorkoutListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_workout_item, parent, false);
        ViewHolder holder = new ViewHolder(view,mOnWorkoutListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        Cursor res = mDb.getWorkoutData();
        res.moveToPosition(pos);
        holder.mTitle.setText(res.getString(1));

        holder.mArrow.setImageResource(R.drawable.arrow_right);
    }

    @Override
    public int getItemCount() {
        Cursor res = mDb.getWorkoutData();
        return res.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTitle;
        private ImageView mArrow;
        private RelativeLayout parentLayout;
        OnWorkoutListener mOnWorkoutListener;

        public ViewHolder(View view, OnWorkoutListener onWorkoutListener){
            super(view);
            mTitle = view.findViewById(R.id.workoutName);
            mArrow = view.findViewById(R.id.arrow);
            parentLayout = view.findViewById(R.id.parent_track);
            this.mOnWorkoutListener = onWorkoutListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view){
            Cursor res = mDb.getWorkoutRow(mTitle.getText().toString());
            res.moveToNext();
            mOnWorkoutListener.onWorkoutClick(res.getInt(0));
        }

        @Override
        public boolean onLongClick(View view) {
            Cursor res = mDb.getWorkoutRow(mTitle.getText().toString());
            res.moveToNext();
            mOnWorkoutListener.onWorkoutLongClick(res.getInt(0));
            return true;
        }
    }

    public interface OnWorkoutListener{
        void onWorkoutClick(int position);
        void onWorkoutLongClick(int position);
    }
}
