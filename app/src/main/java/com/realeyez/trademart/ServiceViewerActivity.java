package com.realeyez.trademart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.gui.components.profile.panels.MediaPanel;
import com.realeyez.trademart.gui.components.profile.panels.MediaPanelImage;
import com.realeyez.trademart.gui.components.profile.panels.MediaPanelVideo;
import com.realeyez.trademart.gui.components.scroll.ScrollDotPanel;
import com.realeyez.trademart.gui.components.scroll.SnapScrollH;
import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.FileUtil;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ServiceViewerActivity extends AppCompatActivity {

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
    private TextView priceLabel;

    private ImageButton likeButton;
    private ImageButton backButton;

    private String username;

    private int serviceId;
    private ArrayList<Integer> mediaIds;
    private ArrayList<MediaPanel> mediaPanels;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        serviceId = intent.getIntExtra("service_id", -1);
        mediaIds = intent.getIntegerArrayListExtra("media_ids");
        username = intent.getStringExtra("username");
        mediaPanels = new ArrayList<>();
        setContentView(R.layout.activity_service_viewer);
        initComponents();
        loadPost();
    }

    private void initComponents(){
        mediaScroll = findViewById(R.id.serviceviewer_media_scroll);
        mediaScrollPanel = findViewById(R.id.serviceviewer_media_scroll_panel);
        mediaCountPanel = findViewById(R.id.serviceviewer_media_dots_panel);
        titleLabel = findViewById(R.id.serviceviewer_title_view);
        descLabel = findViewById(R.id.serviceviewer_desc_view);
        nameLabel = findViewById(R.id.serviceviewer_name_view);
        likesLabel = findViewById(R.id.serviceviewer_like_count);
        priceLabel = findViewById(R.id.serviceviewer_price);

        likeButton = findViewById(R.id.serviceviewer_like_button);
        backButton = findViewById(R.id.serviceviewer_back_button);
        mediaDots = findViewById(R.id.serviceviewer_media_dots_panel);

        snapScroll = new SnapScrollH(mediaScroll);
        addActionListeners();
    }

    private void loadPost(){
        ExecutorService serviceDataExecutor = Executors.newSingleThreadExecutor();
        ExecutorService mediaExecutor = Executors.newSingleThreadExecutor();
        serviceDataExecutor.execute(() -> {
            try {
                loadPostData();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.log("unable to load service data", LogLevel.WARNING);
                finish();
            }
        });
        mediaExecutor.execute(() -> {
            runOnUiThread(() -> {
                dotsPanel = new ScrollDotPanel(this, mediaDots, mediaIds.size());
            });
            for(int mediaId : mediaIds ){
                String path = new StringBuilder()
                    .append("/media/").append(mediaId).toString();
                try {
                    Request request = RequestUtil.createGetRequest(path);
                    Response response = request.sendRequest();
                    String filename = response.getContentDisposition().getField("filename");
                    if(FileUtil.getExtension(filename).equals("m3u8")){
                        runOnUiThread(() -> {
                            String location = new StringBuilder()
                                .append(response.getHost())
                                .append(response.getLocation())
                                .toString();
                            addVideoMedia(Uri.parse(location));
                        });
                    } else {
                        CacheFile cacheFile = CacheFile.cache(getCacheDir(), filename, response.getContentBytes());
                        File file = cacheFile.getFile();
                        runOnUiThread(() -> {
                            addImageMedia(file);
                        });
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                if(mediaPanels.get(0) instanceof MediaPanelVideo){
                    ((MediaPanelVideo)mediaPanels.get(0)).start();
                }
            });
        });
    }

    private String generatePriceString(double price){
        String text = new StringBuilder()
            .append(String.format("₱ %.2f", price))
            .toString();
        return text;
    }

    private String generateLikeString(int likes){
        String text = new StringBuilder()
            .append(likes)
            .toString();
        return text;
    }

    private void loadPostData() throws IOException {
        String path = new StringBuilder()
            .append("/service/").append(serviceId).toString();
        Response response = RequestUtil.sendGetRequest(path);
        try {
            JSONObject json = response.getContentJson();
            runOnUiThread(() -> {
                try{ 
                    titleLabel.setText(json.getString("service_title"));
                    priceLabel.setText(generatePriceString(json.getDouble("service_price")));
                    nameLabel.setText(username);
                    descLabel.setText(json.getString("service_description"));
                    likesLabel.setText(generateLikeString(json.getInt("likes")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private LayoutParams createMediaPanelLayoutParams(){
        return new LayoutParams(
                mediaScroll.getWidth(),
                mediaScroll.getHeight(),
                Gravity.CENTER);
    }

    private void addImageMedia(File image){
        MediaPanelImage panel = new MediaPanelImage(this, createMediaPanelLayoutParams(), Uri.fromFile(image));
        mediaPanels.add(panel);
        mediaScrollPanel.addView(panel.getImageView());
    }

    private void addVideoMedia(Uri video){
        MediaPanelVideo panel = new MediaPanelVideo(this, createMediaPanelLayoutParams());
        mediaPanels.add(panel);
        panel.setMediaUri(video);
        mediaScrollPanel.addView(panel.getPlayerView());
    }

    private void addActionListeners(){
        backButton.setOnClickListener(view -> {
            finish();
        });
        likeButton.setOnClickListener(view -> {
        });
        snapScroll.setOnChangeChildListener((lastChild, curChild) -> {
            dotsPanel.setActive(curChild);
            MediaPanel lastPanel = mediaPanels.get(lastChild);
            if(lastPanel instanceof MediaPanelVideo){
                ((MediaPanelVideo)lastPanel).reset();
            }
            MediaPanel panel = mediaPanels.get(curChild);
            if(panel instanceof MediaPanelVideo){
                ((MediaPanelVideo)panel).start();
            }
        });
    }
};

