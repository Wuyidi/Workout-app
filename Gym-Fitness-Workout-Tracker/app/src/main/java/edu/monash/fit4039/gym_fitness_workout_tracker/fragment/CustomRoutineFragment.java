package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.CreateRoutineActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.ViewCustomRoutineActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.ViewRoutineActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;
import edu.monash.fit4039.gym_fitness_workout_tracker.viewholder.RoutineViewHolder;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomRoutineFragment extends BaseFragment {
    // Instance variable
    private DatabaseReference routineDatabase;
    private FirebaseRecyclerAdapter<Routine, RoutineViewHolder> adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    // Unique Identifier for receiving activity result
    public static final int ADD_ROUTINE_REQUEST = 1;
    private TextView emptyView;
    private ValueEventListener routineListener;


    public CustomRoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_custom_routine, container, false);
        emptyView = (TextView) v.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) v.findViewById(R.id.customRoutine_list);
        // Create database reference
        routineDatabase = FirebaseDatabase.getInstance().getReference().child("custom-routines").child(getUid());
        // Set up Layout Manager, reverse layout
        manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        setUpAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    // Set up FirebaseRecyclerAdapter
    private void setUpAdapter() {
        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        adapter = new FirebaseRecyclerAdapter<Routine, RoutineViewHolder>(Routine.class,
                R.layout.list_routine_item, RoutineViewHolder.class, routineDatabase) {

            @Override
            protected void populateViewHolder(RoutineViewHolder viewHolder, final Routine model, final int position) {
                final String key = this.getRef(position).getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch View Routine Activity
                        Intent intent = new Intent(getActivity(), ViewCustomRoutineActivity.class);
                        intent.putExtra(ViewCustomRoutineActivity.EXTRA_CUSTOM_ROUTINE_KEY, key);
                        startActivity(intent);
                    }

                });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        buildDialog(model, position);
                        return true;
                    }
                });


                // Bind Routine to ViewHolder, setting OnClickListener
                viewHolder.bindToRoutine(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.custom_edit_name_dialog);
                        dialog.setCancelable(false);
                        final EditText nameEditText = (EditText) dialog.findViewById(R.id.dialog_editName);
                        Button saveButton = (Button) dialog.findViewById(R.id.btn_dialog_save);
                        Button cancelButton = (Button) dialog.findViewById(R.id.btn_dialog_cancel);

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String name = nameEditText.getText().toString().trim();
                                if (!name.equals("")) {
                                    Map<String, Object> routineUpdates = new HashMap<String, Object>();
                                    routineUpdates.put("name", name);
                                    routineDatabase.child(key).updateChildren(routineUpdates);
                                    feedback("Update name successfully");
                                    dialog.cancel();

                                } else {
                                    nameEditText.setError("Required");
                                }
                            }
                        });
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                });
            }

        };
    }

    // Create menu and implement method
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addRoutine:
                Intent intent = new Intent(getActivity(), CreateRoutineActivity.class);
                startActivityForResult(intent, ADD_ROUTINE_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ROUTINE_REQUEST) {
            if (resultCode == RESULT_OK) {
                feedback("Create Routine Successfully");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Add a value event listener to read data
        // https://stackoverflow.com/questions/28217436/how-to-show-an-empty-view-with-a-recyclerview
        ValueEventListener listener = new ValueEventListener() {
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
        routineListener = listener;
        routineDatabase.addValueEventListener(routineListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (routineListener != null) {
            routineDatabase.removeEventListener(routineListener);
        }
    }

    // Delete a routine exercise from Firebase database
    private void deleteData(int position) {
        adapter.getRef(position).setValue(null);
        String key = adapter.getRef(position).getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/routine-exercise/" + key);
        reference.setValue(null);
    }

    // Build a dialog for deleting a routine
    private void buildDialog(Routine routine, final int position) {
        // Build a dialog to delete item
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Routine?");
        builder.setMessage("Are you sure you wish to delete " + routine.getName() + "?");
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
}
