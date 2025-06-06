package com.realeyez.trademart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.encryption.Encryptor;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.Content.ContentBuilder;
import com.realeyez.trademart.util.Dialogs;

public class SignUpActivity extends AppCompatActivity {

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

        signUpButton.setOnClickListener(view -> {
            signupButtonAction();
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

    private void showSignupFailedDialog(String message){
        runOnUiThread(() -> {
            Dialogs.showDialog("Oops!", message, this);
        });
    }

    private void signupButtonAction(){
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String reEnter = reEnterField.getText().toString();

        if (!passwordField.getText().toString().equals(reEnter)) {
            reEnterField.setError("Passwords do not match");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Response response = sendSignupRequest(name, password, email);
            try {
                JSONObject json = response.getContentJson();
                String status = json.getString("status");
                String message = json.getString("message");
                if(status.equals("failed")){
                    showSignupFailedDialog(message);
                    return;
                }
                if(status.equals("success")){
                    showLoginPage();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    private Response sendSignupRequest(String username, String password, String email) {
        Encryptor encryptor = new Encryptor();
        String encryptedPassword = encryptor.encrypt(password);
        String saltIv = encryptor.getSaltIV();

        Content content = new ContentBuilder()
            .put("username", username)
            .put("email", email)
            .put("password", encryptedPassword)
            .put("salt_iv", saltIv)
            .build();

        Request request = new Request.RequestBuilder()
            .setPost(content.getContentString())
            .useSSL()
            .setHost(getResources().getString(R.string.host_url))
            .noPort()
            .setPath("/user/signup")
            .setContentType("application/json")
            .build();

        Response response = null;
        try {
            response = request.sendRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void showLoginPage(){
        runOnUiThread(() -> {
            Intent explicitActivity = new Intent(SignUpActivity.this, LoginPageActivity.class);
            startActivity(explicitActivity);
        });
    }

    // Button ratings = findViewById(R.id.RatingCheck);
    // ratings.setOnClickListener(view -> {
    // Intent explicitActivity = new Intent(SignUpActivity.this,
    // RatingViewActivity.class);
    // startActivity(explicitActivity);
    // });

    // Button interests = findViewById(R.id.InterestCheck);
    // interests.setOnClickListener(view -> {
    // Intent explicitActivity = new Intent(SignUpActivity.this,
    // InterestViewActivity.class);
    // startActivity(explicitActivity);
    // });

}
