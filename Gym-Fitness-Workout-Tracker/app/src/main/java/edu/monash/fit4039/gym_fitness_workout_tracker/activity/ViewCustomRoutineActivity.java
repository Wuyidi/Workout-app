package edu.monash.fit4039.gym_fitness_workout_tracker.activity;




import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.RoutineExercise;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.TimerDialog;

public class ViewCustomRoutineActivity extends BaseActivity {

    // Unique Identifier for receiving activity result
    public static final int ADD_ROUTINE_EXERCISE_REQUEST = 1;
    public static final String EXTRA_CUSTOM_ROUTINE_KEY = "routine_key";
    // Instance variable
    private DatabaseReference routineExerciseDatabase;
    private DatabaseReference customRoutineDatabase;
    private ListView listView;
    private FirebaseListAdapter adapter;
    private String routineKey;
    private TextView emptyView;
    private ValueEventListener routineListener;
    private ValueEventListener routineExerciseListener;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_custom_routine);

        Toolbar toolbar = (Toolbar) findViewById(R.id.customRoutineExercise_toolbar);
        setSupportActionBar(toolbar);
        // Get routine key from intent
        routineKey = getIntent().getStringExtra(EXTRA_CUSTOM_ROUTINE_KEY);

        // Get reference from firebase node
        routineExerciseDatabase = FirebaseDatabase.getInstance().getReference().child("routine-exercise").child(routineKey);
        customRoutineDatabase = FirebaseDatabase.getInstance().getReference().child("custom-routines").child(getUid());

        // Get reference from activity
        emptyView = (TextView) findViewById(R.id.customRoutineEmptyView);
        listView = (ListView) findViewById(R.id.customRoutine_exerciseListView);

        // Initialize views
        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        adapter = new FirebaseListAdapter<RoutineExercise>(this, RoutineExercise.class
                , R.layout.list_custom_routine_exercise_item, routineExerciseDatabase) {

            @Override
            protected void populateView(View v, RoutineExercise exercise, final int position) {
                ((TextView) v.findViewById(R.id.list_RoutineExerciseName)).setText(exercise.getExerciseName());
                ((TextView) v.findViewById(R.id.list_sets_reps)).setText(exercise.simpleDescription());
                ImageButton button = (ImageButton) v.findViewById(R.id.btn_edit_exercise);
                button.setFocusable(false);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference reference = adapter.getRef(position);
                        buildEditDialog(reference);
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        // Set Click listener on the list view
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RoutineExercise exercise = (RoutineExercise) listView.getItemAtPosition(position);
                buildDeleteDialog(exercise, position);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TimerDialog timer = new TimerDialog();
                timer.showDialog(ViewCustomRoutineActivity.this);
            }
        });
    }
    // Delete the data from Firebase
    private void deleteData(int position) {
        adapter.getRef(position).setValue(null);
        String key = adapter.getRef(position).getParent().getKey();
        Map<String, Object> routineUpdates = new HashMap<String, Object>();
        routineUpdates.put("exerciseCount", count - 1);
        customRoutineDatabase.child(key).updateChildren(routineUpdates);
    }
    // Build a delete dialog
    private void buildDeleteDialog(RoutineExercise exercise, final int position) {
        // Build a dialog to delete item
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Exercise?");
        builder.setMessage("Are you sure you wish to delete " + exercise.getExerciseName() + "?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteData(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    // Build a edit dialog
    private void buildEditDialog(final DatabaseReference reference) {
        final Dialog dialog = new Dialog(ViewCustomRoutineActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_routine_exercise_dialog);
        dialog.setCancelable(false);

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
                    updateRoutineExercise(reference, set, rep);
                    dialog.cancel();

                }
            }
        });

        dialog.show();
    }

    // Update the particular routine exercise
    private void updateRoutineExercise(DatabaseReference reference, int sets, int reps) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("sets", sets);
        updates.put("reps", reps);
        reference.updateChildren(updates);
    }

    // Create our menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addRoutine:
                Intent intent = new Intent(this, AddRoutineExerciseActivity.class);
                intent.putExtra(AddRoutineExerciseActivity.EXTRA_ADD_ROUTINE_KEY, routineKey);
                startActivityForResult(intent, ADD_ROUTINE_EXERCISE_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ROUTINE_EXERCISE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Map<String, Object> routineUpdates = new HashMap<String, Object>();
                routineUpdates.put("exerciseCount", count + 1);
                customRoutineDatabase.child(routineKey).updateChildren(routineUpdates);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }


    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Exercise object and use the values to update the UI
                Routine routine = dataSnapshot.getValue(Routine.class);
                setTitle(routine.getName());
                count = routine.getExerciseCount();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                feedback("Failed to load exercise.");
            }
        };
        routineListener = listener;
        customRoutineDatabase.child(routineKey).addValueEventListener(listener);
        // Set up empty view
        // https://stackoverflow.com/questions/28217436/how-to-show-an-empty-view-with-a-recyclerview
        routineExerciseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        routineExerciseDatabase.addValueEventListener(routineExerciseListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (routineListener != null) {
            customRoutineDatabase.child(routineKey).removeEventListener(routineListener);
        }

        if (routineExerciseListener != null) {
            routineExerciseDatabase.removeEventListener(routineExerciseListener);
        }
    }
}
