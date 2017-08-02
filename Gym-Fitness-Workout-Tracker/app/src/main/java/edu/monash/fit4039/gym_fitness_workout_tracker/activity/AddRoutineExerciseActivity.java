package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Exercise;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.RoutineExercise;


public class AddRoutineExerciseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // Instance variable
    private ListView exerciseView;
    private DatabaseReference exercisesReference;
    private SearchView searchView;
    private String exerciseName;
    private RoutineExercise routineExercise;
    private String exerciseKey;
    private FirebaseListAdapter adapter;
    public static final String EXTRA_ADD_ROUTINE_KEY = "routine_key";
    private String routineKey;
    private DatabaseReference routineExerciseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine_exercise);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addRoutineExercise_toolbar);
        setSupportActionBar(toolbar);
        routineKey = getIntent().getStringExtra(EXTRA_ADD_ROUTINE_KEY);
        // Get reference from Firebase node
        routineExerciseDatabase = FirebaseDatabase.getInstance().getReference().child("routine-exercise").child(routineKey);
        // Get reference from activity
        exerciseView = (ListView) findViewById(R.id.addExerciseListView);
        exercisesReference = FirebaseDatabase.getInstance().getReference().child("exercises");
        adapter = new FirebaseListAdapter<Exercise>(this, Exercise.class
                , R.layout.list_exercise_item, exercisesReference) {
            @Override
            protected void populateView(View v, Exercise exercise, int position) {
                ((TextView) v.findViewById(R.id.list_exerciseName)).setText(exercise.getName());
                ((TextView) v.findViewById(R.id.list_focusArea)).setText(exercise.getFocusArea());

            }
        };
        exerciseView.setAdapter(adapter);
        exerciseView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_search, menu);
        setSearchView(menu);
        // Create an onQueryText Listener Anonymous class
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Make first query text upper case
                if (newText.length() > 0) {
                    newText = newText.substring(0, 1).toUpperCase() + newText.substring(1);
                }
                Query query = exercisesReference.orderByChild("name").startAt(newText).endAt(String.valueOf(newText) + "\uf8ff");
                FirebaseListAdapter firebaseListAdapter = new FirebaseListAdapter<Exercise>(AddRoutineExerciseActivity.this, Exercise.class
                        , R.layout.list_exercise_item, query) {
                    @Override
                    protected void populateView(View v, Exercise exercise, int position) {
                        ((TextView) v.findViewById(R.id.list_exerciseName)).setText(exercise.getName());
                        ((TextView) v.findViewById(R.id.list_focusArea)).setText(exercise.getFocusArea());

                    }
                };
                exerciseView.setAdapter(firebaseListAdapter);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Set up search view
    private void setSearchView(Menu menu) {
        MenuItem item = menu.getItem(0);
        searchView = new SearchView(getBaseContext());
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("search exercise by name");
        searchView.setSubmitButtonEnabled(false);
        item.setActionView(searchView);

    }

    // Implement onItemClick listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        DatabaseReference reference = adapter.getRef(position);
        exerciseKey = reference.getKey();
        exerciseName = ((Exercise) listView.getItemAtPosition(position)).getName();
        routineExercise = new RoutineExercise(exerciseName);
        buildDialog(routineExercise);

    }

    // Build a dialog to edit reps and sets value
    private void buildDialog(final RoutineExercise exercise) {
        final Dialog dialog = new Dialog(AddRoutineExerciseActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_routine_exercise_dialog);
        dialog.setCancelable(false);
        // Get reference from dialog
        final EditText inputSets = (EditText) dialog.findViewById(R.id.setEditText);
        final EditText inputReps = (EditText) dialog.findViewById(R.id.repEditText);
        Button cancelButton = (Button) dialog.findViewById(R.id.dialog_routineExercise_cancelButton);
        Button saveButton = (Button) dialog.findViewById(R.id.dialog_routineExercise_saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sets = inputSets.getText().toString();
                String reps = inputReps.getText().toString();
                if (TextUtils.isEmpty(sets)) {
                    inputSets.setError("Required");
                } else if (TextUtils.isEmpty(reps)) {
                    inputReps.setError("Required");
                } else {
                    int set = new Integer(inputSets.getText().toString());
                    int rep = new Integer(inputReps.getText().toString());
                    exercise.setSets(set);
                    exercise.setReps(rep);
                    writeNewRoutineExercise(exercise);
                    Intent intent = new Intent();
                    intent.putExtra(ViewRoutineActivity.EXTRA_ROUTINE_KEY, routineKey);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        dialog.show();

    }

    // Write a new routine exercise to firebase
    private void writeNewRoutineExercise(RoutineExercise exercise) {
        DatabaseReference databaseReference = routineExerciseDatabase.child(exerciseKey);
        databaseReference.setValue(exercise);
    }

}
