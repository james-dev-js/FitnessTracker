package com.example.fitnesstracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.R;

public class ExerciseAddDialog extends AppCompatDialogFragment {
    private EditText exerciseName,exerciseDesc, exerciseURL, exerciseReps, exerciseKg;
    private Button btnadd;
    private ExerciseAddDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_add_exercise,null);

        builder.setView(view)
                .setTitle(R.string.addExercise)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                /*.setNeutralButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });*/
        exerciseName = view.findViewById(R.id.add_Exercise_Name);
        exerciseDesc = view.findViewById(R.id.add_Exercise_Desc);
        exerciseURL = view.findViewById(R.id.add_Exercise_URL);
        exerciseReps = view.findViewById(R.id.add_Exercise_Reps);
        exerciseKg = view.findViewById(R.id.add_Exercise_KG);
        btnadd = view.findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exerciseNameAdd = exerciseName.getText().toString();
                String exerciseDescAdd = exerciseDesc.getText().toString();
                String exerciseURLAdd = exerciseURL.getText().toString();
                String exerciseRepsAdd = exerciseReps.getText().toString();
                String exerciseKgAdd = exerciseKg.getText().toString();
                if(!validateName() | !validateDesc() | !validateURL() | !validateReps() | !validateKg()){
                    return;
                }
                listener.applyText(exerciseNameAdd,exerciseDescAdd,exerciseURLAdd,exerciseRepsAdd,exerciseKgAdd);
                dismiss();
            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (ExerciseAddDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement ExerciseAddDialogListener");
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
    public interface ExerciseAddDialogListener{
        void applyText(String exerciseName,String exerciseDesc,String exerciseURL,String exerciseReps,String exerciseKg);
    }
}
