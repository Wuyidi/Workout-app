package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.ViewExerciseActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Exercise;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseFragment extends Fragment implements AdapterView.OnItemClickListener {
    // Instance variable
    private ListView exerciseView;
    private DatabaseReference exercisesReference;
    private SearchView searchView;


    public ExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_exercise, container, false);
        exerciseView = (ListView) currentView.findViewById(R.id.exerciseListView);
        // Connect to Firebase database
        exercisesReference = FirebaseDatabase.getInstance().getReference().child("exercises");
        // Set up FirebaseListAdapter
        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        FirebaseListAdapter adapter = new FirebaseListAdapter<Exercise>(getActivity(), Exercise.class
                , R.layout.list_exercise_item, exercisesReference) {
            @Override
            protected void populateView(View v, Exercise exercise, int position) {
                ((TextView) v.findViewById(R.id.list_exerciseName)).setText(exercise.getName());
                ((TextView) v.findViewById(R.id.list_focusArea)).setText(exercise.getFocusArea());

            }
        };
        exerciseView.setAdapter(adapter);
        exerciseView.setEmptyView(currentView.findViewById(R.id.emptyElement));
        exerciseView.setOnItemClickListener(this);

        return currentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;
        Exercise exercise = (Exercise) listView.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), ViewExerciseActivity.class);
        intent.putExtra("exercise", exercise);
        startActivity(intent);
    }

    // Create our menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                // https://stackoverflow.com/questions/38618953/how-to-do-a-simple-search-in-string-in-firebase-database
                Query query = exercisesReference.orderByChild("name").startAt(newText).endAt(String.valueOf(newText) + "\uf8ff");
                FirebaseListAdapter firebaseListAdapter = new FirebaseListAdapter<Exercise>(getActivity(), Exercise.class
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
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Set up search view
    private void setSearchView(Menu menu) {
        MenuItem item = menu.getItem(0);
        searchView = new SearchView(getContext());
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("search exercise by name");
        searchView.setSubmitButtonEnabled(false);
        item.setActionView(searchView);

    }
}
