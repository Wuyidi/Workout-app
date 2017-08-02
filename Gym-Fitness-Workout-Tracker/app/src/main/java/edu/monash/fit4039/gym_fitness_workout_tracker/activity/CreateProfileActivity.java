package edu.monash.fit4039.gym_fitness_workout_tracker.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.monash.fit4039.gym_fitness_workout_tracker.R;

public class CreateProfileActivity extends BaseActivity implements View.OnClickListener {
    // Instance variable
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText heightEditText;
    private EditText weightEditText;
    private RadioGroup radioGroup;
    private Button saveButton;
    private String gender;
    private DatabaseReference profileReference;
    private static final String REQUIRED = "Required";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        gender = "unknown";

        // Get reference to users node
        profileReference = FirebaseDatabase.getInstance().getReference().child("users").child(getUid());

        // Get reference from activity
        nameEditText = (EditText) findViewById(R.id.profile_nameEditText);
        ageEditText = (EditText) findViewById(R.id.profile_ageEditText);
        heightEditText = (EditText) findViewById(R.id.profile_heightEditText);
        weightEditText = (EditText) findViewById(R.id.profile_weightEditText);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        saveButton = (Button) findViewById(R.id.profile_saveButton);
        saveButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                // This will get the radio button that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // If the radio button that has changed in check state is now checked
                if (checkedRadioButton.isChecked()) {
                    gender = (String) checkedRadioButton.getText();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            String name = nameEditText.getText().toString().trim();
            int age = new Integer(ageEditText.getText().toString());
            double height = new Double(heightEditText.getText().toString());
            double weight = new Double(weightEditText.getText().toString());
            updateUser(name, gender, age, height, weight);
            // Return previous page
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            if (TextUtils.isEmpty(String.valueOf(ageEditText.getText())))
                ageEditText.setError(REQUIRED);
            if (TextUtils.isEmpty(String.valueOf(heightEditText.getText())))
                heightEditText.setError(REQUIRED);
            if (TextUtils.isEmpty(String.valueOf(weightEditText.getText())))
                weightEditText.setError(REQUIRED);
        }
    }


    private void updateUser(String name, String gender, int age, double height, double weight) {
        // updating the user via child nodes
        if (!TextUtils.isEmpty(name))
            profileReference.child("name").setValue(name);
        if (!TextUtils.isEmpty(gender))
            profileReference.child("gender").setValue(gender);
        profileReference.child("age").setValue(age);
        profileReference.child("height").setValue(height);
        profileReference.child("weight").setValue(weight);

    }
}
