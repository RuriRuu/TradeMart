package com.realeyez.trademart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realeyez.trademart.gui.components.profile.ShowcasePanel;
import com.realeyez.trademart.gui.components.profile.ShowcaseRow;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Encoder;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class ProfilePageActivity extends AppCompatActivity {

    private FloatingActionButton newPostButton;
    private TextView usernameLabel;
    private TextView postsLabel;
    private TextView ratingLabel;
    private TextView completedJobLabel;
    private ImageView profileImageView;
    private LinearLayout mediaPanel;

    private ShowcasePanel showcasePanel;
    private ArrayList<ShowcaseRow> showcaseRows;

    private User user;
    private int userId;

    private int postCount;
    private int completedJobCount;
    private double rating;

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

    @Override
    public void onResume(){
        super.onResume();
        initProfile();
    }

    private void initProfile(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            loadProfile();
            loadPosts();

            runOnUiThread(() -> { initProfileComponents(); });

        });

    }

    private void loadProfile(){
        String path = new StringBuilder()
            .append("/user/profile/")
            .append(userId)
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            initUser(json);
            postCount = json.getInt("post_count");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            showUnableToLoad();
        }
    }

    private void loadPosts(){
        Response response = null;
        try {
            response = RequestUtil.sendGetRequest("/media/" + 93490);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String encodedData = null;
        try {
            JSONObject json = response.getContentJson();
            encodedData = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        byte[] data = Encoder.decodeBase64(encodedData);
        File file = new File(getCacheDir(), "temp");

        try (FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        runOnUiThread(() -> {
            Uri uri = Uri.fromFile(file);
            profileImageView.setImageURI(uri);
        });

    }

    private void loadPostMedia(int postId){
        String path = new StringBuilder()
            .append("/post/")
            .append(postId)
            .append("/media")
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            JSONArray ids = json.getJSONArray("media_ids");
            for (int i = 0; i < ids.length(); i++) {
                int id = ids.getInt(i);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            showUnableToLoad();
        }
    }

    private void initProfileComponents(){
        usernameLabel.setText(user.getUsername());
        postsLabel.setText(new StringBuilder().append(postCount).toString());
        ratingLabel.setText(new StringBuilder().append(String.format("%.1f", rating)).toString());
        completedJobLabel.setText(new StringBuilder().append(completedJobCount).toString());
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
        mediaPanel = findViewById(R.id.media_panel);
        rating = postCount = completedJobCount = 0;
        showcaseRows = new ArrayList<>();
        showcasePanel = new ShowcasePanel(this, mediaPanel);

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
