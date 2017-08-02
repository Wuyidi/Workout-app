package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;

public class CreateRoutineActivity extends BaseActivity implements View.OnClickListener {
    // Instance variable
    private EditText inputName;
    private DatabaseReference routineDatabase;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.createRoutine_toolbar);
        setSupportActionBar(toolbar);

        // Get reference from activity
        inputName = (EditText) findViewById(R.id.buildRoutineNameEdit);
        saveButton = (Button) findViewById(R.id.btn_save_routineName);
        // Get reference to custom routines node
        routineDatabase = FirebaseDatabase.getInstance().getReference().child("custom-routines").child(getUid());
        saveButton.setOnClickListener(this);
    }

    // Write a new routine to Firebase
    private void writeNewRoutine(String name) {
        Routine routine = new Routine(name, 0);
        routineDatabase.push().setValue(routine, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    feedback("Routine couldn't be saved " + databaseError.getMessage());
                } else {
                    feedback("Routine saved successfully");
                }
            }
        });
    }

    // Implement onClick listener
    @Override
    public void onClick(View v) {
        String name = inputName.getText().toString().trim();
        if (!name.equals("")) {
            writeNewRoutine(name);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            inputName.setError("Required");
        }
    }
}
