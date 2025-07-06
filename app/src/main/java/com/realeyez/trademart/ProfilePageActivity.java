package com.realeyez.trademart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realeyez.trademart.gui.components.profile.ShowcasePanel;
import com.realeyez.trademart.gui.components.profile.ShowcaseRow;
import com.realeyez.trademart.post.PostData;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePageActivity extends AppCompatActivity {

    private FloatingActionButton newPostButton;
    private TextView usernameLabel;
    private TextView postsLabel;
    private TextView ratingLabel;
    private TextView completedJobLabel;
    private ImageView profileImageView;
    private ScrollView scrollView;

    private LinearLayout mediaPanel;

    private ShowcasePanel showcasePanel;
    private ArrayList<ShowcaseRow> showcaseRows;

    private User user;
    private int userId;

    private int postCount;
    private int completedJobCount;
    private double rating;

    private ArrayList<Integer> loadedPostIds;

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
        setContentView(R.layout.activity_profile_page);
        initComponents();
        // initProfile();
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
            runOnUiThread(() -> { initProfileComponents(); });

            loadPosts();
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
        Response postIdResponse = sendLoadMorePostRequest();
        try {
            JSONObject postIdResponseJson = postIdResponse.getContentJson();
            Logger.log("received response for loading more posts: " + postIdResponseJson.toString(), LogLevel.INFO);
            JSONArray arr = postIdResponseJson.getJSONArray("post_ids");
            Logger.log("THE LENGTH OF THE ARRAY WAS: " + arr.length(), LogLevel.INFO);
            for (int i = 0; i < arr.length(); i++) {
                ArrayList<Integer> mediaIds = getPostMediaId(arr.getInt(i));
                int postId = arr.getInt(i);
                PostData postData = new PostData.Builder()
                    .setPostId(postId)
                    .setMediaIds(mediaIds)
                    .setUsername(user.getUsername())
                    .build();
                File imageFile = getThumbnailFileFromMedia(mediaIds.get(0));
                loadedPostIds.add(arr.getInt(i));
                runOnUiThread(() -> {
                    Uri uri = Uri.fromFile(imageFile);
                    showcasePanel.addImage(uri, postData);
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private File getThumbnailFileFromMedia(int mediaId){
        File file = null;
        try {
            Response response = RequestUtil.sendGetRequest("/media/thumbnail/" + mediaId);
            String filename = response.getContentDispositionField("filename");
            byte[] data = response.getContentBytes();

            CacheFile cacheFile = CacheFile.cache(getCacheDir(), filename, data);
            file = cacheFile.getFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private File getFileFromMedia(int mediaId){
        File file = null;
        try {
            Response response = RequestUtil.sendGetRequest("/media/" + mediaId);
            String filename = response.getContentDispositionField("filename");
            byte[] data = response.getContentBytes();

            CacheFile cacheFile = CacheFile.cache(getCacheDir(), filename, data);
            file = cacheFile.getFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private ArrayList<Integer> getPostMediaId(int postId){
        ArrayList<Integer> ids = new ArrayList<>();
        String path = new StringBuilder()
            .append("/post/")
            .append(postId)
            .append("/media")
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONArray arr = response.getContentJson().getJSONArray("media_ids");
            for (int i = 0; i < arr.length(); i++) {
                ids.add(arr.getInt(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

    private Response sendLoadMorePostRequest(){
        JSONObject json = new JSONObject();
        try {
            json.put("post_ids", new JSONArray(loadedPostIds));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Content content = new Content.ContentBuilder()
            .parseJson(json.toString());
        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/post/user/" + userId, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
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
        scrollView = findViewById(R.id.profile_media_scroll_view);
        rating = postCount = completedJobCount = 0;
        showcaseRows = new ArrayList<>();
        showcasePanel = new ShowcasePanel(this, mediaPanel);
        loadedPostIds = new ArrayList<>();
        // scrollY = scrollView.getScrollY();

        // ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#E91E63"));

        addOnClickListeners();
        // scrollView.setOnScrollChangeListener((view, x, y, ox, oy) -> {
        //     if(!scrollView.canScrollVertically(1)){
        //         loadPosts();
        //     }
        // });
    }

    private void newPostButtonAction(){
        Intent explicitActivity = new Intent(ProfilePageActivity.this, CreatePostActivity.class);
        startActivity(explicitActivity);
    }

    private void addOnClickListeners(){
        newPostButton.setOnClickListener(view -> {
            newPostButtonAction();
        });
    }

}
