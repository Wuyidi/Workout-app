package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Timer;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment implements View.OnClickListener {

    // Instance variable
    private TextView timerDisplay;
    private ToggleButton toggleButton;
    private Button resetButton;
    private int cnt = 0;
    private Timer timer;
    private Boolean isRunning = false;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        // Get reference from activity
        timerDisplay = (TextView) v.findViewById(R.id.display);
        toggleButton = (ToggleButton) v.findViewById(R.id.timer_toggleButton);
        resetButton = (Button) v.findViewById(R.id.timer_resetButton);
        toggleButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        timer = new Timer(timerDisplay, cnt);
        return v;
    }


    // Implement onClick listener
    @Override
    public void onClick(View v) {
        if (v.getId() == toggleButton.getId()) {
            if (toggleButton.isChecked()) {

                timer.stop();
                isRunning = false;
                toggleButton.setTextOff("STOP");


            } else {
                timer.start();
                isRunning = true;
                toggleButton.setTextOn("START");
            }
        } else if (v.getId() == resetButton.getId()) {
            if (isRunning) {
                timer.stop();
                isRunning = false;
                timer.reset();
                toggleButton.setChecked(true);
                toggleButton.setTextOn("START");
            } else {

                timer.reset();

            }

        }
    }

}
