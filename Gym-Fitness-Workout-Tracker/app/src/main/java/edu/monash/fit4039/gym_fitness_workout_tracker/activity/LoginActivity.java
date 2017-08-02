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

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    // Instance variable
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressBar progressBar;
    private Button loginButton;
    private Button signUpButton;
    private Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        // Get reference from activity
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progressBar);
        loginButton = (Button) findViewById(R.id.btn_login);
        signUpButton = (Button) findViewById(R.id.btn_signUp);
        resetButton = (Button) findViewById(R.id.btn_reset_password);

        // Set onClick listener on button
        loginButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    // http://www.coderefer.com/firebaseui-android-firebase-database/
    @Override
    public void onClick(View v) {
        if (v.getId() == loginButton.getId()) {
            String email = inputEmail.getText().toString().trim();
            final String password = inputPassword.getText().toString().trim();
            // Validate user input
            if (TextUtils.isEmpty(email)) {
                inputEmail.setError("Required");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                inputPassword.setError("Required");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            // authenticate user
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                if (password.length() < 6) {
                                    inputPassword.setError(getString(R.string.minimum_password));
                                } else {
                                    feedback(getString(R.string.auth_failed));
                                }
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    });

        } else if (v.getId() == signUpButton.getId()) {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

        } else if (v.getId() == resetButton.getId()) {
            startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}
