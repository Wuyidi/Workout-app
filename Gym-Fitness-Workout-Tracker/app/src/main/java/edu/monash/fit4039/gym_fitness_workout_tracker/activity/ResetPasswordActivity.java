package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {

    // Instance variable
    private EditText inputEmail;
    private Button resetButton;
    private Button backButton;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Get reference from the activity
        inputEmail = (EditText) findViewById(R.id.reset_email);
        resetButton = (Button) findViewById(R.id.btn_reset);
        backButton = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.reset_progressBar);

        auth = FirebaseAuth.getInstance();

        // Set onClick listener on buttons
        resetButton.setOnClickListener(this);
        backButton.setOnClickListener(this);


    }

    // Implement onClickListener
    @Override
    public void onClick(View v) {
        if (v.getId() == resetButton.getId()) {
            String email = inputEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                feedback("Enter your registered email id");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                feedback("We have sent you instructions to reset your password!");
                            } else {
                                feedback("Failed to send reset email!");
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        } else if (v.getId() == backButton.getId()) {
            finish();
        }
    }


}
