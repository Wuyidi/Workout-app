package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;



import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.ViewRoutineActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;
import edu.monash.fit4039.gym_fitness_workout_tracker.viewholder.DefaultRoutineViewHolder;



/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultRoutineFragment extends BaseFragment {

    // Instance variable
    private DatabaseReference routineDatabase;
    private FirebaseRecyclerAdapter<Routine, DefaultRoutineViewHolder> adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;


    public DefaultRoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_default_routine, container, false);

        // Get reference from fragment
        recyclerView = (RecyclerView) v.findViewById(R.id.routine_list);
        recyclerView.setHasFixedSize(true);

        // Create database reference
        routineDatabase = FirebaseDatabase.getInstance().getReference().child("routines");
        // Set up Layout Manager, reverse layout
        manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        setUpAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

    // Set up FirebaseRecyclerAdapter with the database reference
    // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
    private void setUpAdapter() {
        adapter = new FirebaseRecyclerAdapter<Routine, DefaultRoutineViewHolder>(Routine.class,
                R.layout.list_default_routine_item, DefaultRoutineViewHolder.class, routineDatabase) {

            @Override
            protected void populateViewHolder(DefaultRoutineViewHolder viewHolder, Routine model, int position) {
                final String key = this.getRef(position).getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch View Routine Activity
                        Intent intent = new Intent(getActivity(), ViewRoutineActivity.class);
                        intent.putExtra(ViewRoutineActivity.EXTRA_ROUTINE_KEY, key);
                        startActivity(intent);
                    }

                });

                // Bind Routine to ViewHolder, setting OnClickListener
                viewHolder.bindToRoutine(model);
            }

        };


    }

}
