package edu.monash.fit4039.gym_fitness_workout_tracker.model;

/**
 * Using algorithm to filter out values that has close approximation to steps.
 */

// http://www.gadgetsaint.com/android/create-pedometer-step-counter-android/#.WSl9TxN95sM
public class SensorFilter {

    public SensorFilter() {
    }

    // Sum up value from array
    public static float sum(float[] array) {
        float retval = 0;
        for (int i = 0; i < array.length; i++) {
            retval += array[i];
        }
        return retval;
    }

    // Cross value from two array
    public static float[] cross(float[] arrayA, float[] arrayB) {
        float[] retArray = new float[3];
        retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1];
        retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2];
        retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0];
        return retArray;
    }

    // Returns the correctly rounded positive square root of a double value
    public static float norm(float[] array) {
        float retval = 0;
        for (int i = 0; i < array.length; i++) {
            retval += array[i] * array[i];
        }
        return (float) Math.sqrt(retval);
    }

    // Multiply the variable from two array and sum up
    public static float dot(float[] a, float[] b) {
        float retval = a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
        return retval;
    }

    // Normalize an array
    public static float[] normalize(float[] array) {
        float[] retval = new float[array.length];
        float norm = norm(array);
        for (int i = 0; i < array.length; i++) {
            retval[i] = array[i] / norm;
        }
        return retval;
    }
}
