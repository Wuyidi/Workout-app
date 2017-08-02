package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.CreateProfileActivity;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Profile;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment {

    // Unique Identifier for receiving activity result
    public static final int EDIT_PROFILE_REQUEST = 1;
    // Instance variable
    private FloatingActionButton button;
    private DatabaseReference profileReference;
    private ValueEventListener profileListener;
    private TextView nameView;
    private TextView ageView;
    private TextView heightView;
    private TextView weightView;
    private TextView genderView;
    private Spinner spinner;
    private TextView calorieView;
    private Profile currentProfile;
    private ArrayList<String> factorList;


    public ProfileFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        // setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get reference from the fragment
        button = (FloatingActionButton) v.findViewById(R.id.editButton);
        nameView = (TextView) v.findViewById(R.id.frag_nameText);
        ageView = (TextView) v.findViewById(R.id.frag_ageText);
        heightView = (TextView) v.findViewById(R.id.frag_heightText);
        weightView = (TextView) v.findViewById(R.id.frag_weightText);
        genderView = (TextView) v.findViewById(R.id.frag_genderText);
        spinner = (Spinner) v.findViewById(R.id.frag_factorSpinner);
        calorieView = (TextView) v.findViewById(R.id.frag_totalCalories);

        // Set up ArrayAdapter for activity level options
        initializeFactorArray();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, factorList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, true);
        View view = spinner.getSelectedView();
        ((TextView) view).setTextColor(getResources().getColor(R.color.spinner_text));
        ((TextView) view).setTextSize(13);

        // Set onClick listener on a button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateProfileActivity.class);
                startActivityForResult(intent, EDIT_PROFILE_REQUEST);
            }
        });


        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST) {
            if (resultCode == RESULT_OK) {
                feedback("Edit Profile successfully!");
            } else {
                feedback("Profile hasn't saved!");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        profileReference = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());
        profileListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentProfile = dataSnapshot.getValue(Profile.class);
                initialize(currentProfile);
                // Set up a spinner
                // https://developer.android.com/guide/topics/ui/controls/spinner.html
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) parent.getChildAt(0)).setTextSize(13);
                        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.spinner_text));

                        calculateCalories(currentProfile);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        profileReference.addValueEventListener(profileListener);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (profileListener != null) {
            profileReference.removeEventListener(profileListener);
        }
    }

    // Initialize a profile
    private void initialize(Profile profile) {
        nameView.setText("Name:   " + profile.getName());
        ageView.setText("Age:   " + String.valueOf(profile.getAge()));
        heightView.setText("Height:   " + profile.formatHeight());
        weightView.setText("Weight:   " + profile.formatWeight());
        genderView.setText("Gender:   " + profile.getGender());
        calculateCalories(profile);
    }

    // Initialize a factor array
    private void initializeFactorArray() {
        factorList = new ArrayList<>();
        factorList.add(getString(R.string.sedentary));
        factorList.add(getString(R.string.lightly_active));
        factorList.add(getString(R.string.moderately_active));
        factorList.add(getString(R.string.very_active));
    }

    // Set up text view to show daily calories
    private void calculateCalories(Profile profile) {
        if (spinner.getSelectedItemPosition() == 0) {
            calorieView.setText(profile.getDailyCalorie(Profile.SEDENTARY) + " CALs");
        } else if (spinner.getSelectedItemPosition() == 1) {
            calorieView.setText(profile.getDailyCalorie(Profile.LIGHTLY_ACTIVE) + " CALs");
        } else if (spinner.getSelectedItemPosition() == 2) {
            calorieView.setText(profile.getDailyCalorie(Profile.MODERATELY_ACTIVE) + " CALs");
        } else if (spinner.getSelectedItemPosition() == 3) {
            calorieView.setText(profile.getDailyCalorie(Profile.ACTIVE) + " CALs");
        }

    }
}
