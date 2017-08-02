package edu.monash.fit4039.gym_fitness_workout_tracker.fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {
    // Instance variable
    private EditText inputNewEmail;
    private EditText inputNewPassword;
    private Button changeEmailButton;
    private Button changePassButton;
    private Button updateEmailButton;
    private Button updatePassButton;
    private Button signOutButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;

    public SettingFragment() {
        // Required empty public constructor
    }


    // http://www.coderefer.com/firebaseui-android-firebase-database/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.show();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        // Create a authStateListener
        auth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        };


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);
        // Get reference from the fragment
        inputNewEmail = (EditText) v.findViewById(R.id.newEmail);
        inputNewPassword = (EditText) v.findViewById(R.id.newPassword);
        changeEmailButton = (Button) v.findViewById(R.id.changeEmailButton);
        changePassButton = (Button) v.findViewById(R.id.changePassButton);
        updateEmailButton = (Button) v.findViewById(R.id.changeEmail);
        updatePassButton = (Button) v.findViewById(R.id.changePass);
        signOutButton = (Button) v.findViewById(R.id.btn_sign_out);
        progressBar = (ProgressBar) v.findViewById(R.id.setting_progressBar);

        // Set visibility
        updateEmailButton.setVisibility(View.GONE);
        updatePassButton.setVisibility(View.GONE);
        inputNewEmail.setVisibility(View.GONE);
        inputNewPassword.setVisibility(View.GONE);

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        changePassButton.setOnClickListener(this);
        changeEmailButton.setOnClickListener(this);
        updateEmailButton.setOnClickListener(this);
        updatePassButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        return v;
    }

    // Implement onClick listener
    @Override
    public void onClick(View v) {
        if (v.getId() == signOutButton.getId()) {
            auth.signOut();
        } else if (v.getId() == changeEmailButton.getId()) {
            updateEmailButton.setVisibility(View.VISIBLE);
            updatePassButton.setVisibility(View.GONE);
            inputNewPassword.setVisibility(View.GONE);
            inputNewEmail.setVisibility(View.VISIBLE);


        } else if (v.getId() == changePassButton.getId()) {
            updatePassButton.setVisibility(View.VISIBLE);
            updateEmailButton.setVisibility(View.GONE);
            inputNewEmail.setVisibility(View.GONE);
            inputNewPassword.setVisibility(View.VISIBLE);

        } else if (v.getId() == updateEmailButton.getId()) {
            progressBar.setVisibility(View.VISIBLE);
            String newEmail = inputNewEmail.getText().toString().trim();
            if (user != null && !newEmail.equals("")) {
                user.updateEmail(newEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    feedback(getString(R.string.update_email_success));
                                    auth.signOut();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    feedback(getString(R.string.update_email_failed));
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            } else {
                inputNewEmail.setError(getString(R.string.update_email_error));
                progressBar.setVisibility(View.GONE);
            }

        } else if (v.getId() == updatePassButton.getId()) {
            progressBar.setVisibility(View.VISIBLE);
            String newPassword = inputNewPassword.getText().toString().trim();
            if (user != null && !newPassword.equals("")) {
                if (newPassword.length() < 6) {
                    inputNewPassword.setError(getString(R.string.minimum_password));
                    progressBar.setVisibility(View.GONE);
                } else {
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        feedback(getString(R.string.update_pass_success));
                                        auth.signOut();
                                        progressBar.setVisibility(View.GONE);
                                    } else {
                                        feedback(getString(R.string.update_pass_failed));
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            } else {
                inputNewPassword.setError(getString(R.string.update_pass_error));
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null)
            auth.removeAuthStateListener(authStateListener);
    }
}
