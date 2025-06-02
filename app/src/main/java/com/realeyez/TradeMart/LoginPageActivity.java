package com.realeyez.TradeMart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginPageActivity extends AppCompatActivity {
    EditText enteredName;
    EditText enteredPassword;
    Button goToSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enteredName = findViewById(R.id.enteredName);
        enteredPassword = findViewById(R.id.enteredPassword);
        goToSignUp = findViewById(R.id.goToSignUp);

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
