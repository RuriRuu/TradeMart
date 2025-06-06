package com.realeyez.trademart;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.user.User.UserBuilder;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePageActivity extends AppCompatActivity {

    private FloatingActionButton newPostButton;
    private TextView usernameLabel;
    private TextView postsLabel;
    private TextView ratingLabel;
    private TextView completedJobLabel;
    private ImageView profileImageView;

    private User user;
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            Logger.log("bruh instance bundle is null", LogLevel.CRITICAL);
        }
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        if(userId == -1){
            showUnableToLoad();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        initComponents();
        initProfile();
    }

    private void initProfile(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String path = new StringBuilder()
                .append("/user/profile/")
                .append(userId)
                .toString();
            try {
                Response response = RequestUtil.sendGetRequest(path);
                JSONObject json = response.getContentJson();
                initUser(json);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                showUnableToLoad();
            }

            runOnUiThread(() -> { initProfileComponents(); });

        });

    }

    private void initProfileComponents(){
        usernameLabel.setText(user.getUsername());
    }

    private void initUser(JSONObject json){
        try {
            user = new User.UserBuilder()
                .setId(json.getInt("user_id"))
                .setUsername(json.getString("username"))
                .setEmail(json.getString("email"))
                .build();
        } catch (JSONException e) {
            e.printStackTrace();
            showUnableToLoad();
        }
    }

    private void showUnableToLoad(){
        runOnUiThread(() -> {
            Dialogs.showErrorDialog("Unable to load the profile!", this);
            finish();
        });
    }

    private void initComponents(){
        newPostButton = findViewById(R.id.profile_post_fab);
        usernameLabel = findViewById(R.id.profile_name_view);
        postsLabel = findViewById(R.id.profile_posts_count_view);
        ratingLabel = findViewById(R.id.profile_rating_view);
        completedJobLabel = findViewById(R.id.profile_jobs_completed_count);
        profileImageView = findViewById(R.id.profile_image_view);
        addOnClickListeners();
    }

    private void newPostButtonAction(View view){
        Intent explicitActivity = new Intent(ProfilePageActivity.this, CreatePostActivity.class);
        startActivity(explicitActivity);
    }

    private void addOnClickListeners(){
        newPostButton.setOnClickListener(view -> {
            newPostButtonAction(view);
        });
    }

}
