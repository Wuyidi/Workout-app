package edu.monash.fit4039.gym_fitness_workout_tracker.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Profile;
import edu.monash.fit4039.gym_fitness_workout_tracker.model.Routine;


public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    // Instance Variable
    private EditText inputEmail;
    private EditText inputPassword;
    private Button signInButton;
    private Button registerButton;
    private Button resetPasswordButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference();

        // Get reference from the activity
        registerButton = (Button) findViewById(R.id.btn_register);
        signInButton = (Button) findViewById(R.id.btn_sign_in);
        inputEmail = (EditText) findViewById(R.id.signUp_email);
        inputPassword = (EditText) findViewById(R.id.signUp_password);
        resetPasswordButton = (Button) findViewById(R.id.btn_forget_password);
        progressBar = (ProgressBar) findViewById(R.id.signUp_progressBar);

        registerButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == registerButton.getId()) {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            // Validate user input
            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("Required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("Required");
                return;
            }

            if (password.length() < 6) {
                feedback("Password too short, enter minimum 6 characters!");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // Create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            feedback("createUserWithEmail:onComplete: " + task.isSuccessful());
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                feedback("Authentication failed. " + task.getException());
                            } else {
                                onAuthSuccess(task.getResult().getUser());
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                finish();
                            }

                        }
                    });
        } else if (v.getId() == signInButton.getId()) {
            finish();

        } else if (v.getId() == resetPasswordButton.getId()) {
            startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
        }

    }

    // Convert the email to user name
    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    // Write a new user to Firebase
    private void createNewUser(String userId, String name) {
        Profile profile = new Profile(name, 0, 0, 0, "unknown");
        database.child("users").child(userId).setValue(profile);
    }

    // Create a new user
    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        createNewUser(user.getUid(), username);

    }


}
