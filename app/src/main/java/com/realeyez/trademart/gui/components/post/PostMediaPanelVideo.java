package com.realeyez.trademart.gui.components.post;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.net.Uri;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PostMediaPanelVideo implements PostMediaPanel {

    private PlayerView playerView;
    private ExoPlayer player;
    private Uri videoUri;

    public PostMediaPanelVideo(Context context, LayoutParams layoutParams){
        playerView = new PlayerView(context);
        player = new ExoPlayer.Builder(context).build();
        playerView.setLayoutParams(layoutParams);
        playerView.setPlayer(player);
    }

    public PostMediaPanelVideo(Context context, int width, int height){
        playerView = new PlayerView(context);
        player = new ExoPlayer.Builder(context).build();
        LayoutParams params = new LayoutParams(width, height);
        playerView.setLayoutParams(params);
        playerView.setPlayer(player);
    }

    public void togglePlay(){
        if(player.isPlaying())
            player.pause();
        else
            player.play();
    }

    public void setMediaUri(Uri uri){
        this.videoUri = uri;
        player.setMediaItem(MediaItem.fromUri(uri));
    }

    public void prepare(){
        player.prepare();
    }

    public void pause(){
        player.pause();
    }
    
    public void play(){
        player.play();
    }

    public void reset(){
        player.stop();
        player.clearMediaItems();
    }

    public void start(){
        if(player.getMediaItemCount() == 0){
            player.setMediaItem(MediaItem.fromUri(videoUri));
        }
        prepare();
        play();
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    @Override
    public Uri getUri(){
        return videoUri;
    }
    
}
