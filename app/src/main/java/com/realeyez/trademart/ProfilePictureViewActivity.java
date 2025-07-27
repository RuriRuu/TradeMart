package com.realeyez.trademart;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.realeyez.trademart.request.requestor.ProfilePictureRequestor;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilePictureViewActivity extends AppCompatActivity {

    private ImageView profilePicture;
    private ImageButton backButton;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_picture);

        profilePicture = findViewById(R.id.Profile_Picture);
        backButton = findViewById(R.id.Prof_Pic_Back_Button);
        executorService = Executors.newSingleThreadExecutor();

        backButton.setOnClickListener(v -> finish());
        loadProfilePicture();
    }

    private void loadProfilePicture() {
        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not available", Toast.LENGTH_SHORT).show();
            return;
        }

        File cacheDir = getCacheDir();

        executorService.execute(() -> {
            try {
                Uri profilePictureUri = ProfilePictureRequestor.sendRequest(userId, cacheDir);
                runOnUiThread(() -> profilePicture.setImageURI(profilePictureUri));
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to load profile picture", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}