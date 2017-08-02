package edu.monash.fit4039.gym_fitness_workout_tracker.viewholder;


import android.view.View;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;

/**
 * Created by wyd on 9/6/17.
 */

public class DefaultRoutineViewHolder extends RoutineViewHolder {

    public DefaultRoutineViewHolder(View itemView) {
        super(itemView);
    }
    // Override the bindToRoutine method
    public void bindToRoutine(Routine routine) {
        nameView.setText(routine.getName());
        countView.setText(routine.countExercise());
        setImage(routine.getName(), backgroundView);
    }
}
