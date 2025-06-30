package com.realeyez.trademart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.Encoder;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.PlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    private PlayerView vidView;
    private ExoPlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_player);
        initComponents();
        loadVideo();
    }

    private void initComponents(){
        vidView = findViewById(R.id.videoplayer_video_view);
        player = new ExoPlayer.Builder(this).build();
        vidView.setPlayer(player);
    }

    private void loadVideo(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Request request = null;
            try {
                request = RequestUtil.createGetRequest("/media/video/Z1diR3lXQTJmaUlVaUUvRmVGT0ZXZz09.m3u8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri vidUri = request.getUri();
            runOnUiThread(() -> {
                player.setMediaItem(MediaItem.fromUri(vidUri));
                player.prepare();
                player.play();
            });
        });
    }


}
