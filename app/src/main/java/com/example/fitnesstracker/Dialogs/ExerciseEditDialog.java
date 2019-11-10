package com.example.fitnesstracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.Database.DatabaseHelper;
import com.example.fitnesstracker.R;

public class ExerciseEditDialog extends AppCompatDialogFragment {
    private EditText exerciseName,exerciseDesc, exerciseURL, exerciseReps, exerciseKg;
    private ExerciseEditDialogListener listener;
    private Button btnadd;
    private int exerciseid;
    private DatabaseHelper db;

    public ExerciseEditDialog(int ExerciseID, Context context){
        this.db = new DatabaseHelper(context);
        this.exerciseid = ExerciseID;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_add_exercise,null);
        exerciseName = view.findViewById(R.id.add_Exercise_Name);
        exerciseDesc = view.findViewById(R.id.add_Exercise_Desc);
        exerciseURL = view.findViewById(R.id.add_Exercise_URL);
        exerciseReps = view.findViewById(R.id.add_Exercise_Reps);
        exerciseKg = view.findViewById(R.id.add_Exercise_KG);
        Cursor res = db.getExerciseRow(exerciseid);
        res.moveToNext();
        exerciseName.setText(res.getString(1));
        exerciseDesc.setText(res.getString(2));
        exerciseURL.setText(res.getString(3));
        exerciseReps.setText(res.getString(4));
        exerciseKg.setText(res.getString(5));

        builder.setView(view)
                .setTitle(R.string.editExercise)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        btnadd = view.findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseNameEdit = exerciseName.getText().toString();
                String exerciseDescEdit = exerciseDesc.getText().toString();
                String exerciseURLEdit = exerciseURL.getText().toString();
                String exerciseRepsEdit = exerciseReps.getText().toString();
                String exerciseKgEdit = exerciseKg.getText().toString();
                if(!validateName() | !validateDesc() | !validateURL() | !validateReps() | !validateKg()){
                    return;
                }
                listener.ediText(exerciseid,exerciseNameEdit,exerciseDescEdit,exerciseURLEdit,exerciseRepsEdit,exerciseKgEdit);
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (ExerciseEditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExerciseEditDialogListener");
        }
    }

    private boolean validateName(){
        String name = exerciseName.getText().toString();
        if(name.isEmpty())
        {
            exerciseName.setError("Field can't be empty");
            return false;
        }else {
            exerciseName.setError(null);
            return true;
        }
    }

    private boolean validateDesc(){
        String desc = exerciseDesc.getText().toString();
        if(desc.isEmpty())
        {
            exerciseDesc.setError("Field can't be empty");
            return false;
        }else {
            exerciseDesc.setError(null);
            return true;
        }
    }

    private boolean validateURL(){
        String url = exerciseURL.getText().toString();
        if(url.isEmpty())
        {
            exerciseURL.setError("Field can't be empty");
            return false;
        } else if(!Patterns.WEB_URL.matcher(url).matches()){
            exerciseURL.setError("It must be a proper url");
            return false;
        }
        else {
            exerciseURL.setError(null);
            return true;
        }
    }

    private boolean validateReps(){
        String reps = exerciseReps.getText().toString();
        if(reps.isEmpty())
        {
            exerciseReps.setError("Field can't be empty");
            return false;
        }else {
            exerciseReps.setError(null);
            return true;
        }
    }
    private boolean validateKg(){
        String kg = exerciseKg.getText().toString();
        if(kg.isEmpty())
        {
            exerciseKg.setError("Field can't be empty");
            return false;
        }else {
            exerciseKg.setError(null);
            return true;
        }
    }
    public interface ExerciseEditDialogListener{
        void ediText(int id, String exerciseName,String exerciseDesc,String exerciseURL,String exerciseReps,String exerciseKg);
    }
}
