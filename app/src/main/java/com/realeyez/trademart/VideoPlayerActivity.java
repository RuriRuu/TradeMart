package com.realeyez.trademart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

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
    }

    private void loadVideo(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Uri vidUri = Uri.fromFile(createTempVidFile());
            runOnUiThread(() -> {
                player.setMediaItem(MediaItem.fromUri(vidUri));
                player.prepare();
                player.play();
                // vidView.setVideoURI(vidUri);
                // vidView.start();
            });
        });
    }


    private File createTempVidFile(){
        File file = null;
        try {
            Response response = RequestUtil.sendGetRequest("/videothing");
            byte[] data = response.getContentBytes();

            file = new File(getCacheDir(), "temp_vid_placeholder.mp4");

            try (FileOutputStream writer = new FileOutputStream(file)) {
                writer.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
