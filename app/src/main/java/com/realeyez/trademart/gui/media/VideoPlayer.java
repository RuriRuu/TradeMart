package com.realeyez.trademart.gui.media;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class VideoPlayer {

    private PlayerView playerView;
    private ExoPlayer player;
    private Context context;

    public VideoPlayer(Context context, int width, int height){
        playerView = new PlayerView(context);
        player = new ExoPlayer.Builder(context).build();
        LayoutParams params = new LayoutParams(width, height);
        playerView.setLayoutParams(params);
        player.prepare();
    }

    public VideoPlayer(Context context){
        playerView = new PlayerView(context);
        player = new ExoPlayer.Builder(context).build();
        player.prepare();
    }

    public void togglePlay(){
        if(player.isPlaying())
            player.pause();
        else
            player.play();
    }

    public void pause(){
        player.pause();
    }
    
    public void play(){
        player.play();
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

}
