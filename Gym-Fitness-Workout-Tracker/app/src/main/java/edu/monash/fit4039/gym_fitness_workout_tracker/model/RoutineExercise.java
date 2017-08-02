package edu.monash.fit4039.gym_fitness_workout_tracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyd on 1/5/17.
 */

public class RoutineExercise implements Parcelable {
    // Instance variable
    public String exerciseName;
    public int reps;
    public int sets;

    public RoutineExercise() {
        // Default constructor required for calls to DataSnapshot.getValue(Exercise.class)
    }

    public RoutineExercise(String exerciseName, int reps, int sets) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.sets = sets;
    }

    public RoutineExercise(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    // Setter and getter method
    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String simpleDescription() {
        return sets + " sets of " + reps + " reps";
    }

    @Exclude
    // Convert the variable to hash map
    public Map<String, Object> toMap() {
        HashMap<String, Object> results = new HashMap<>();
        results.put("name", exerciseName);
        results.put("reps", reps);
        results.put("sets", sets);
        return results;
    }

    protected RoutineExercise(Parcel in) {
        exerciseName = in.readString();
        reps = in.readInt();
        sets = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exerciseName);
        dest.writeInt(reps);
        dest.writeInt(sets);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RoutineExercise> CREATOR = new Parcelable.Creator<RoutineExercise>() {
        @Override
        public RoutineExercise createFromParcel(Parcel in) {
            return new RoutineExercise(in);
        }

        @Override
        public RoutineExercise[] newArray(int size) {
            return new RoutineExercise[size];
        }
    };
}
