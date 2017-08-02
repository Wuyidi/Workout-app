package edu.monash.fit4039.gym_fitness_workout_tracker.model;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by wyd on 16/4/17.
 */

public class Routine implements Serializable {

    // Instance variable
    public String name;
    public int exerciseCount;


    public Routine() {
        // Default constructor required for calls to DataSnapshot.getValue(Routine.class)
    }

    public Routine(String name, int exerciseCount) {
        this.name = name;
        this.exerciseCount = exerciseCount;
    }

    // Setter and getter method
    public Routine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getExerciseCount() {
        return exerciseCount;
    }

    public void setExerciseCount(int exerciseCount) {
        this.exerciseCount = exerciseCount;
    }


    @Exclude
    // Convert the variable to hash map
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("exerciseCount", exerciseCount);
        return result;
    }

    public String countExercise() {
        return "Contains " + exerciseCount + " exercises";
    }


}
