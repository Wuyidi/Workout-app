package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

/**
 * Created by wyd on 28/4/17.
 */

public class BaseFragment extends Fragment {
    // Create a feedback
    public void feedback(String msg) {
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
        View v = toast.getView();
        v.setBackgroundResource(R.drawable.custom_toast);
        toast.setView(v);
        toast.show();
    }

    // Get current user ID from firebase
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
