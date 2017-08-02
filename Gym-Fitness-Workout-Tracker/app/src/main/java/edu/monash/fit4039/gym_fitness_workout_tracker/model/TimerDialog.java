package edu.monash.fit4039.gym_fitness_workout_tracker.model;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

/**
 * Created by wyd on 5/5/17.
 */

public class TimerDialog implements View.OnClickListener {

    // Instance variable
    private TextView timerDisplay;
    private ToggleButton toggleButton;
    private Button resetButton;
    private int cnt = 0;
    private Timer timer;
    private Boolean isRunning = false;


    // Build a dialog
    // https://stackoverflow.com/questions/13341560/how-to-create-a-custom-dialog-box-in-android
    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_timer_dialog);

        timerDisplay = (TextView) dialog.findViewById(R.id.showTimerText);
        toggleButton = (ToggleButton) dialog.findViewById(R.id.dialog_toggleButton);
        resetButton = (Button) dialog.findViewById(R.id.dialog_resetButton);
        toggleButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        timer = new Timer(timerDisplay, cnt);
        dialog.show();
    }


    // Set on click listener for each button
    // https://developer.android.com/guide/topics/ui/controls/togglebutton.html
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
