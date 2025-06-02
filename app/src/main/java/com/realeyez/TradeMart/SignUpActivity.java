package com.realeyez.TradeMart;
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
    EditText name;
    EditText email;
    EditText password;
    EditText reEnter;
    Button signUpButton;
    Button returnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        reEnter = findViewById(R.id.reEnter);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String ReEnter = reEnter.getText().toString();

                if (!password.equals(reEnter)) {
                    reEnter.setError("Passwords do not match");
                }

                User newUser = new User(Name, Email, Password);
                users.add(newUser);

                returnToLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SignUpActivity.this, LoginPageActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }


        });

/*
        Button ratings = findViewById(R.id.RatingCheck);
        ratings.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(SignUpActivity.this, RatingViewActivity.class);
            startActivity(explicitActivity);
        });

        Button interests = findViewById(R.id.InterestCheck);
        interests.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(SignUpActivity.this, InterestViewActivity.class);
            startActivity(explicitActivity);
        });
*/
    }
}
