package com.realeyez.trademart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.realeyez.trademart.gui.components.scroll.SnapScrollH;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dimension;
import com.realeyez.trademart.util.FileUtil;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
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
    private LinearLayout mediaScrollPanel;
    private LinearLayout mediaCountPanel;

    private TextView titleLabel;
    private TextView descLabel;
    private TextView nameLabel;
    private TextView likesLabel;

    private ImageButton likeButton;
    private ImageButton backButton;

    private int postId;
    private ArrayList<Integer> mediaIds;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        postId = intent.getIntExtra("post_id", -1);
        mediaIds = intent.getIntegerArrayListExtra("media_ids");
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

        this.snapScroll = new SnapScrollH(mediaScroll);

        addActionListeners();
    }

    private void loadPost(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
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
        });
    }

    private Dimension getMediaScrollDimensions(){
        return new Dimension(
                mediaScroll.getWidth(),
                mediaScroll.getHeight()
                );
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
    }
};
