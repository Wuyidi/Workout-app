package edu.monash.fit4039.gym_fitness_workout_tracker.model;

import android.os.Handler;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by wyd on 27/4/17.
 */

// http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/
public class Timer implements Runnable {

    // Instance variable
    private TextView textView;
    private Handler handler;
    private int cnt;

    // Default constructor
    public Timer() {
    }

    public Timer(TextView textView, int cnt) {
        this.textView = textView;
        textView.setText(getTime(0));
        this.cnt = cnt;
        handler = new Handler();
    }

    // Implement runnable
    @Override
    public void run() {
        textView.setText(getTime(cnt++));
        handler.postDelayed(this, 1000);
    }

    // Methods to control timer
    // A start method
    public void start() {

        if (cnt == 0) {
            this.run();
        } else {
            handler.postDelayed(this, 1000);
        }
    }
    // A stop method
    public void stop() {
        handler.removeCallbacks(this);
    }

    // A reset method
    public void reset() {
        cnt = 0;
        textView.setText(getTime(cnt));
    }
    // Convert the count number to time format
    private String getTime(int cnt) {
        int hour = cnt / 3600;
        int min = cnt % 3600 / 60;
        int sec = cnt % 60;
        return String.format(Locale.CANADA, "%02d:%02d:%02d", hour, min, sec);
    }
}
