package com.realeyez.trademart;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.realeyez.trademart.gui.sheets.ProfilePictureSheet;
import com.realeyez.trademart.post.PostData;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.requestor.ProfilePictureRequestor;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.user.User;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.DimensionsUtil;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePageActivity extends AppCompatActivity {

    private FloatingActionButton newPostButton;
    private TextView usernameLabel;
    private TextView postsLabel;
    private TextView ratingLabel;
    private TextView completedJobLabel;
    private ImageView profileImageView;
    private ScrollView scrollView;

    private ImageButton chatButton;
    private ImageButton backButton;

    private LinearLayout mediaPanel;
    private LinearLayout completedJobsPanel;

    private ShowcasePanel showcasePanel;
    private ArrayList<ShowcaseRow> showcaseRows;

    private User user;
    private int userId;

    private int postCount;
    private int completedJobCount;
    private double rating;
    private String username;

    private ArrayList<Integer> loadedPostIds;
    private ArrayList<Integer> loadedEmployerIds;

    // TODO: add chat button and probably put back the tool bar
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        if(userId == -1){
            showUnableToLoad();
        }
        setContentView(R.layout.activity_profile_page);
        initComponents();
        registerUpdateSheetResultAction();
        // initProfile();
    }

    @Override
    public void onResume(){
        super.onResume();
        initProfile();
    }

    private void registerUpdateSheetResultAction(){
        getSupportFragmentManager().setFragmentResultListener("update_result", 
                this, (key, result) -> {
                    if(!result.getBoolean("success")){
                        return;
                    }
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        loadProfilePicture();
                    });
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
        backButton = findViewById(R.id.profile_back_button);
        chatButton = findViewById(R.id.profile_chat_button);
        completedJobsPanel = findViewById(R.id.profile_completed_jobs_panel);

        rating = postCount = completedJobCount = 0;
        showcaseRows = new ArrayList<>();
        showcasePanel = new ShowcasePanel(this, mediaPanel);
        loadedPostIds = new ArrayList<>();
        loadedEmployerIds = new ArrayList<>();
        addOnClickListeners();
    }

    private void initProfile(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            loadProfile();
            loadProfilePicture();
            runOnUiThread(() -> { initProfileComponents(); });
            loadUserRating();
            loadCompletedJobs();
            loadPosts();
        });
    }

    private void loadCompletedJobs(){
        String path = new StringBuilder()
            .append("/jobs/user/")
            .append(userId)
            .append("/completedjobs")
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            JSONObject data = json.getJSONObject("data");
            JSONArray arr = data.getJSONArray("completed_jobs");
            int count = arr.length();
            String countText = new StringBuilder().append(count).toString();
            Logger.logi("count: " + count);
            runOnUiThread(() -> completedJobLabel.setText(countText));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject job = arr.getJSONObject(i);
                int employerId = job.getInt("employer_id");
                if(loadedEmployerIds.contains(employerId)) continue;
                addCompleteJobProfileImage(employerId);
                loadedEmployerIds.add(employerId);
                // JobViewerData jobViewerData = JobViewerDataRequestor.sendRequest(jobId, employerId);
                // addCompleteJobProfileImage(jobViewerData, employerId);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserRating(){
        try {
            String path = new StringBuilder()
                .append("/rate/user/")
                .append(userId)
                .append("/jobs/rating")
                .toString();

            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            double rating = json.getJSONObject("data").getDouble("rating");
            String ratingStr = String.format("%.2f", rating);
            runOnUiThread(() -> ratingLabel.setText(ratingStr));
        } catch(JSONException | IOException e){
            e.printStackTrace();
        }
    }

    private LayoutParams buildCompletedJobProfileView(){
        float density = DimensionsUtil.getScreenDensity(this);
        int width = (int) (70*density);
        int height = (int) (70*density);
        int margin = (int) (5*density);
        LayoutParams params = new LayoutParams(width, height, 1);
        params.setMargins(margin, margin, margin, margin);
        return params;
    }

    private void addCompleteJobProfileImage(int employerId){
        try {
            Uri pfpUri = ProfilePictureRequestor.sendRequest(employerId, getCacheDir());
            runOnUiThread(() -> {
                CircleImageView pfp = new CircleImageView(this);
                pfp.setLayoutParams(buildCompletedJobProfileView());
                pfp.setImageURI(pfpUri);
                pfp.setOnClickListener(view -> {
                    onCompletedJobProfileImageClicked(employerId);
                });
                completedJobsPanel.addView(pfp);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // private void addCompleteJobProfileImage(JobViewerData data, int userId){
    //     try {
    //         Uri pfpUri = ProfilePictureRequestor.sendRequest(userId, getCacheDir());
    //         runOnUiThread(() -> {
    //             CircleImageView pfp = new CircleImageView(this);
    //             pfp.setLayoutParams(buildCompletedJobProfileView());
    //             pfp.setImageURI(pfpUri);
    //             pfp.setOnClickListener(view -> {
    //                 onCompletedJobProfileImageClicked(data);
    //             });
    //             completedJobsPanel.addView(pfp);
    //         });
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    private void onCompletedJobProfileImageClicked(int employerId){
        Intent intent = new Intent(this, ProfilePageActivity.class);
        intent.putExtra("user_id", employerId);
        // intent.putExtra("job_id", data.getJobId());
        // intent.putExtra("media_ids", data.getMediaIds());
        // intent.putExtra("username", data.getUsername());
        startActivity(intent);
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

    private void loadProfilePicture(){
        String path = new StringBuilder()
            .append("/user/")
            .append(userId)
            .append("/avatar")
            .toString();
        Response response = null;
        try {
            response = RequestUtil.sendGetRequest(path);
        } catch (FileNotFoundException e) {
            Logger.log("User does not have a profile picture", LogLevel.INFO);
            return;
        } catch (IOException e) {
            Logger.log("Something went wrong regarding loading profile picture", LogLevel.INFO);
            e.printStackTrace();
            return;
        }
        CacheFile cacheFile = CacheFile.newFile(getCacheDir(),
                response.getContentDispositionField("filename"), 
                response.getContentBytes());
        File file = cacheFile.getFile();
        runOnUiThread(() -> {
            profileImageView.setImageURI(Uri.fromFile(file));
        });
    }

    private void loadPosts(){
        Response postIdResponse = sendLoadMorePostRequest();
        try {
            JSONObject postIdResponseJson = postIdResponse.getContentJson();
            Logger.log("received response for loading more posts: ".concat(postIdResponseJson.toString()), LogLevel.INFO);
            JSONArray arr = postIdResponseJson.getJSONArray("post_ids");
            Logger.log(String.format("THE LENGTH OF THE ARRAY WAS: %d", arr.length()), LogLevel.INFO);
            for (int i = 0; i < arr.length(); i++) {
                int postId = arr.getInt(i);
                ArrayList<Integer> mediaIds = getPostMediaId(postId);
                // TODO: this woudn't be necessary if posts were finished getting setup before they are loaded here. probs should figure that out.
                if(mediaIds.size() == 0){
                    continue;
                }
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
            Response response = RequestUtil.sendGetRequest(String.format("/media/thumbnail/%d", mediaId));
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
            Response response = RequestUtil.sendGetRequest(String.format("/media/%d", mediaId));
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
            response = RequestUtil.sendPostRequest(String.format("/post/user/%d", userId), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initProfileComponents(){
        usernameLabel.setText(user.getUsername());
        postsLabel.setText(new StringBuilder().append(postCount).toString());
        ratingLabel.setText(new StringBuilder().append(String.format("%.1f", rating)).toString());
        completedJobLabel.setText(new StringBuilder().append(completedJobCount).toString());
    }

    private void initUser(JSONObject json){
        try {
            username = json.getString("username");
            user = new User.UserBuilder()
                .setId(json.getInt("user_id"))
                .setUsername(username)
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

    private void newPostButtonAction(){
        Intent explicitActivity = new Intent(ProfilePageActivity.this, CreatePostActivity.class);
        startActivity(explicitActivity);
    }

    private void showProfilePictureSheet(){
        ProfilePictureSheet sheet = new ProfilePictureSheet(userId);
        sheet.show(getSupportFragmentManager(), ProfilePictureSheet.TAG);
    }

    private void addOnClickListeners(){
        newPostButton.setOnClickListener(view -> {
            newPostButtonAction();
        });
        profileImageView.setOnClickListener(view -> {
            showProfilePictureSheet();
        });

        backButton.setOnClickListener(view -> {
            finish();
        });

        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MessagingActivity.class);
            intent.putExtra("user_id", userId);
            intent.putExtra("convo_id", -1);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

}
