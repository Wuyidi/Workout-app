package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

/**
 * Created by wyd on 28/4/17.
 */

public class BaseActivity extends AppCompatActivity {

    // Get current user ID from Firebase
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    // Show a feedback
    public void feedback(String msg) {
        Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        View v = toast.getView();
        v.setBackgroundResource(R.drawable.custom_toast);
        toast.setView(v);
        toast.show();
    }


}
