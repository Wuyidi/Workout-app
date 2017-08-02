package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;



import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.StepDetector;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.StepListener;

import static android.content.Context.SENSOR_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements StepListener, SensorEventListener{
    // Instance variable
    private Button startButton;
    private Button stopButton;
    private TextView stepView;
    private SensorManager sensorManager;
    private Sensor accelerate;
    private StepDetector simpleStepDetector;
    private int numSteps;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();
        // Inflate the layout for this fragment
        View currentView = inflater.inflate(R.layout.fragment_home, container, false);
        startButton = (Button) currentView.findViewById(R.id.main_startButton);
        stopButton = (Button) currentView.findViewById(R.id.main_stopButton);
        stepView = (TextView) currentView.findViewById(R.id.steps);


        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerate = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numSteps = 0;
                sensorManager.registerListener(HomeFragment.this, accelerate, SensorManager.SENSOR_DELAY_FASTEST);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorManager.unregisterListener(HomeFragment.this);
            }
        });


        return currentView;
    }


    // Implement stepListener
    @Override
    public void step(long timeNs) {
        numSteps ++;
        stepView.setText(numSteps + " steps");


    }

    // Implement sensorEventListener
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
