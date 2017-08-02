package edu.monash.fit4039.gym_fitness_workout_tracker.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;

/**
 * Created by wyd on 3/5/17.
 */


// https://github.com/firebase/quickstart-android/blob/master/database/app/src/main/java/com/google/firebase/quickstart/database/viewholder/PostViewHolder.java
public class RoutineViewHolder extends RecyclerView.ViewHolder {

    // Instance variable
    public TextView nameView;
    public TextView countView;
    public ImageButton editButton;
    public ImageView backgroundView;


    public RoutineViewHolder(View itemView) {
        super(itemView);
        // Get reference from activity
        nameView = (TextView) itemView.findViewById(R.id.routineNameText);
        countView = (TextView) itemView.findViewById(R.id.exerciseCountText);
        backgroundView = (ImageView) itemView.findViewById(R.id.cardView_background);
        editButton = (ImageButton) itemView.findViewById(R.id.edit_routineName);
    }

    // Bind the button and text View to the routine
    public void bindToRoutine(Routine routine, View.OnClickListener editClickListener) {
        nameView.setText(routine.getName());
        countView.setText(routine.countExercise());
        editButton.setOnClickListener(editClickListener);
        setImage(routine.getName(), backgroundView);
    }
    // Set up image for different routine
    public void setImage(String name, ImageView imageView) {
        if (name.equals("Fat Off Muscle On")) {
            imageView.setImageResource(R.drawable.fat_off_muscle_on);
        } else if (name.equals("Full Body Weight Loss")) {
            imageView.setImageResource(R.drawable.loss_weight);
        } else if (name.equals("Full Body Strength")) {
            imageView.setImageResource(R.drawable.full_body_strength);
        } else if (name.equals("Core Strength")) {
            imageView.setImageResource(R.drawable.core_strength);
        } else if (name.equals("Cardio")) {
            imageView.setImageResource(R.drawable.cardio);
        } else if (name.equals("Fit in 5 Minutes")) {
            imageView.setImageResource(R.drawable.fit_five_min);
        } else {
            imageView.setImageResource(R.drawable.workout);
        }
    }
}
