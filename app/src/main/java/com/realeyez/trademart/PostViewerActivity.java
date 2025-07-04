package com.realeyez.trademart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.gui.components.scroll.ScrollDotPanel;
import com.realeyez.trademart.gui.components.scroll.SnapScrollH;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PostViewerActivity extends AppCompatActivity {

    private HorizontalScrollView mediaScroll;
    private SnapScrollH snapScroll;
    private ScrollDotPanel dotsPanel;
    private LinearLayout mediaDots;
    private LinearLayout mediaScrollPanel;
    private LinearLayout mediaCountPanel;

    private TextView titleLabel;
    private TextView descLabel;
    private TextView nameLabel;
    private TextView likesLabel;

    private ImageButton likeButton;
    private ImageButton backButton;

    private String username;

    private int postId;
    private ArrayList<Integer> mediaIds;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        postId = intent.getIntExtra("post_id", -1);
        mediaIds = intent.getIntegerArrayListExtra("media_ids");
        username = intent.getStringExtra("username");
        setContentView(R.layout.activity_post_viewer);
        initComponents();
        loadPost();
    }

    private void initComponents(){
        mediaScroll = findViewById(R.id.postviewer_media_scroll);
        mediaScrollPanel = findViewById(R.id.postviewer_media_scroll_panel);
        mediaCountPanel = findViewById(R.id.postviewer_media_dots_panel);
        titleLabel = findViewById(R.id.postviewer_title_view);
        descLabel = findViewById(R.id.postviewer_desc_view);
        nameLabel = findViewById(R.id.postviewer_name_view);
        likesLabel = findViewById(R.id.postviewer_like_count);

        likeButton = findViewById(R.id.postviewer_like_button);
        backButton = findViewById(R.id.postviewer_back_button);
        mediaDots = findViewById(R.id.postviewer_media_dots_panel);

        snapScroll = new SnapScrollH(mediaScroll);
        addActionListeners();
    }

    private void loadPost(){
        ExecutorService postDataExecutor = Executors.newSingleThreadExecutor();
        ExecutorService mediaExecutor = Executors.newSingleThreadExecutor();
        postDataExecutor.execute(() -> {
            try {
                loadPostData();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log("unable to load post data", LogLevel.WARNING);
                finish();
            }
        });
        mediaExecutor.execute(() -> {
            for(int mediaId : mediaIds ){
                String path = new StringBuilder()
                    .append("/media/").append(mediaId).toString();
                try {
                    Response response = RequestUtil.sendGetRequest(path);
                    String filename = response.getContentDisposition().getField("filename");
                    CacheFile cacheFile = CacheFile.cache(getCacheDir(), filename, response.getContentBytes());
                    runOnUiThread(() -> {
                        addImageMedia(cacheFile.getFile());
                    });
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            dotsPanel = new ScrollDotPanel(this, mediaDots, mediaIds.size());
        });
    }

    private void loadPostData() throws IOException {
        String path = new StringBuilder()
            .append("/post/").append(postId).toString();
        Response response = RequestUtil.sendGetRequest(path);
        try {
            JSONObject json = response.getContentJson();
            titleLabel.setText(json.getString("title"));
            nameLabel.setText(username);
            descLabel.setText(json.getString("description"));
            likesLabel.setText(json.getString("likes"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addImageMedia(File image){
        ImageView imageView = new ImageView(this);
        LayoutParams params = new LayoutParams(
                mediaScroll.getWidth(), 
                mediaScroll.getHeight(), 
                Gravity.CENTER);
        Logger.log("media scroll width: " + mediaScroll.getWidth() , LogLevel.INFO);
        Logger.log("media scroll height: " + mediaScroll.getHeight() , LogLevel.INFO);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setLayoutParams(params);
        imageView.setImageURI(Uri.fromFile(image));
        mediaScrollPanel.addView(imageView);
    }

    // probably should make something to make player have persistent access
    private void addVideoMedia(File image){
        PlayerView playerView = new PlayerView(this);
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        ImageView imageView = new ImageView(this);
        LayoutParams params = new LayoutParams(
                mediaScroll.getWidth(), 
                mediaScroll.getHeight(), 
                Gravity.CENTER);
        imageView.setLayoutParams(params);
        imageView.setImageURI(Uri.fromFile(image));
    }

    private void addActionListeners(){
        backButton.setOnClickListener(view -> {
            finish();
        });
        likeButton.setOnClickListener(view -> {
        });
        snapScroll.setOnCangeChildListener(curChild -> {
            dotsPanel.setActive(curChild);
        });
    }
};
