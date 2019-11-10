package com.example.fitnesstracker.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.fitnesstracker.R;

public class ExerciseAddDialog extends AppCompatDialogFragment {
    private EditText exerciseName,exerciseDesc, exerciseURL, exerciseReps, exerciseKg;
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
                })
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String exerciseNameAdd = exerciseName.getText().toString();
                        String exerciseDescAdd = exerciseDesc.getText().toString();
                        String exerciseURLAdd = exerciseURL.getText().toString();
                        String exerciseRepsAdd = exerciseReps.getText().toString();
                        String exerciseKgAdd = exerciseKg.getText().toString();
                        listener.applyText(exerciseNameAdd,exerciseDescAdd,exerciseURLAdd,exerciseRepsAdd,exerciseKgAdd);
                    }
                });
        exerciseName = view.findViewById(R.id.add_Exercise_Name);
        exerciseDesc = view.findViewById(R.id.add_Exercise_Desc);
        exerciseURL = view.findViewById(R.id.add_Exercise_URL);
        exerciseReps = view.findViewById(R.id.add_Exercise_Reps);
        exerciseKg = view.findViewById(R.id.add_Exercise_KG);


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

    public interface ExerciseAddDialogListener{
        void applyText(String exerciseName,String exerciseDesc,String exerciseURL,String exerciseReps,String exerciseKg);
    }
}
