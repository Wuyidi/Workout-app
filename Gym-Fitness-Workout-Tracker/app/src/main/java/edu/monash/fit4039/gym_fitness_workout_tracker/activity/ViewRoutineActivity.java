package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.RoutineExercise;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.TimerDialog;

public class ViewRoutineActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    // Unique Identifier for receiving activity result
    public static final String EXTRA_ROUTINE_KEY = "routine_key";
    // Instance variable
    private DatabaseReference routineExerciseReference;
    private String routineKey;
    private ValueEventListener routineListener;
    private DatabaseReference routineReference;
    private ListView listView;
    private FirebaseListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routine);
        ActionBar actionBar = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.routineExercise_toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.routine_exerciseListView);

        // Get routine key from intent
        routineKey = getIntent().getStringExtra(EXTRA_ROUTINE_KEY);
        if (routineKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_ROUTINE_KEY");
        }

        // Initialize database
        routineExerciseReference = FirebaseDatabase.getInstance().getReference().child("routine-exercise").child(routineKey);

        // Initialize views
        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        adapter = new FirebaseListAdapter<RoutineExercise>(this, RoutineExercise.class
                , R.layout.list_routine_exercise_item, routineExerciseReference) {

            @Override
            protected void populateView(View v, RoutineExercise exercise, int position) {
                ((TextView) v.findViewById(R.id.list_RoutineExerciseName)).setText(exercise.getExerciseName());
                ((TextView) v.findViewById(R.id.list_sets_reps)).setText(exercise.simpleDescription());
            }


        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Add value event listener to routine
        routineReference = FirebaseDatabase.getInstance().getReference()
                .child("routines").child(routineKey);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Routine object and use the values to update the UI
                Routine routine = dataSnapshot.getValue(Routine.class);
                setTitle(routine.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                feedback("Failed to load routine.");
            }
        };
        routineListener = listener;
        routineReference.addValueEventListener(routineListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (routineListener != null) {
            routineExerciseReference.removeEventListener(routineListener);
        }
    }

    // Implement item click listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TimerDialog timer = new TimerDialog();
        timer.showDialog(this);
    }


}
