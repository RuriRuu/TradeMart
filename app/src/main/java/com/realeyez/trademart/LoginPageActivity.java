package com.realeyez.trademart;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.encryption.Encryptor;
import com.realeyez.trademart.gui.dialogs.LoadingDialog;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.util.Dialogs;

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
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        enteredName = findViewById(R.id.enteredName);
        enteredPassword = findViewById(R.id.enteredPassword);
        goToSignUp = findViewById(R.id.goToSignUp);
        loginButton = findViewById(R.id.loginButton);

        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(view -> {
            loginButtonAction();
        });
    }

    private void loginButtonAction() {
        String username = enteredName.getText().toString();
        String password = enteredPassword.getText().toString();

        boolean noBuenoInput = false;
        if (username.isBlank()) {
            enteredName.setError("Do not leave empty");
            noBuenoInput = true;
        }
        if (password.isBlank()) {
            enteredPassword.setError("Do not leave empty");
            noBuenoInput = true;
        }
        if (noBuenoInput) {
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LoadingDialog dialog = new LoadingDialog(this);
            runOnUiThread(() -> dialog.show());
            Response response = sendLoginRequest(username, password);
            try {
                JSONObject json = response.getContentJson();
                String status = json.getString("status");
                String message = json.getString("message");
                if (status.equals("failed")) {
                    dialog.close();
                    showSignupFailedDialog(message);
                    return;
                }
                if (status.equals("success")) {
                    setupUser(json.getJSONObject("user_data"));
                    showMainPage(dialog);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }

    private void setupUser(JSONObject userJson) {
        try {
            int userId = userJson.getInt("user_id");
            String username = userJson.getString("username");
            String email = userJson.getString("email");

            ResourceRepository resources = ResourceRepository.getResources();
            resources.setCurrentUser(new User.UserBuilder()
                    .setId(userId)
                    .setUsername(username)
                    .setEmail(email)
                    .build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Response sendLoginRequest(String username, String password) {
        Encryptor encryptor = new Encryptor();
        String encryptedPassword = encryptor.encrypt(password);
        String saltIv = encryptor.getSaltIV();

        Content content = new Content.ContentBuilder()
                .put("username", username)
                .put("password", encryptedPassword)
                .put("salt_iv", saltIv)
                .build();

        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/user/login", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void showMainPage(LoadingDialog dialog) {
        runOnUiThread(() -> {
            Intent explicitActivity = new Intent(LoginPageActivity.this, MainActivity.class);
            startActivity(explicitActivity);
            dialog.close();
        });
    }

    private void showSignupFailedDialog(String message) {
        runOnUiThread(() -> {
            Dialogs.showDialog("Oops!", message, this);
        });
    }

}
