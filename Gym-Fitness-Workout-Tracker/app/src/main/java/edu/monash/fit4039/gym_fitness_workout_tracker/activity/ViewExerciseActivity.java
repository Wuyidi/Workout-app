package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Exercise;

public class ViewExerciseActivity extends AppCompatActivity {

    // Instance variables
    private TextView exerciseNameText;
    private TextView exerciseDifficultyText;
    private TextView exerciseImpactLevelText;
    private TextView exerciseFocusAreaText;
    private TextView exerciseUrlText;
    private Exercise currentExercise;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);
        Intent intent = getIntent();
        currentExercise = intent.getParcelableExtra("exercise");
        // Get references to TextView from activity
        exerciseNameText = (TextView) findViewById(R.id.exerciseNameText);
        exerciseDifficultyText = (TextView) findViewById(R.id.exerciseDifficultyText);
        exerciseImpactLevelText = (TextView) findViewById(R.id.exerciseImpactText);
        exerciseFocusAreaText = (TextView) findViewById(R.id.exerciseFocusAreaText);
        exerciseUrlText = (TextView) findViewById(R.id.exerciseUrlText);
        exerciseNameText.setText(currentExercise.getName());
        exerciseDifficultyText.setText(currentExercise.getDifficulty());
        exerciseImpactLevelText.setText(currentExercise.getImpactLevel());
        exerciseFocusAreaText.setText(currentExercise.getFocusArea());
        exerciseUrlText.setText(currentExercise.getUrl());

        exerciseNameText.setText(currentExercise.getName());
        exerciseDifficultyText.setText(currentExercise.getDifficulty());
        exerciseFocusAreaText.setText(currentExercise.getFocusArea());
        exerciseImpactLevelText.setText(currentExercise.getImpactLevel());

    }

}
