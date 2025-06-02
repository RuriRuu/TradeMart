package com.realeyez.trademart;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private ArrayList<User> users = new ArrayList<User>();
    EditText nameField;
    EditText emailField;
    EditText passwordField;
    EditText reEnterField;
    Button signUpButton;
    Button returnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        reEnterField = findViewById(R.id.reEnter);
        signUpButton = findViewById(R.id.signUpButton);
        returnToLogin = findViewById(R.id.returnToLogin);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String reEnter = reEnterField.getText().toString();

                if (!passwordField.getText().toString().equals(reEnter)) {
                    reEnterField.setError("Passwords do not match");
                }

                User newUser = new User(name, email, password);
                users.add(newUser);
            }

        });

        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


        // Button ratings = findViewById(R.id.RatingCheck);
        // ratings.setOnClickListener(view -> {
        //     Intent explicitActivity = new Intent(SignUpActivity.this, RatingViewActivity.class);
        //     startActivity(explicitActivity);
        // });

        // Button interests = findViewById(R.id.InterestCheck);
        // interests.setOnClickListener(view -> {
        //     Intent explicitActivity = new Intent(SignUpActivity.this, InterestViewActivity.class);
        //     startActivity(explicitActivity);
        // });

}
