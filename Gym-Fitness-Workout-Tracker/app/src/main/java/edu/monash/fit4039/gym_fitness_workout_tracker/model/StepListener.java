package edu.monash.fit4039.gym_fitness_workout_tracker.model;

/**
 * Created by wyd on 17/5/17.
 */

// http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.WSl9TxN95sM
public interface StepListener {

    // listen to alerts about steps being detected
    public void step(long timeNs);
}
