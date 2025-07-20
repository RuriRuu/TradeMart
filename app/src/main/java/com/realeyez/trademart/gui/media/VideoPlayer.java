package com.realeyez.trademart.gui.media;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class VideoPlayer extends PlayerView {

    private ExoPlayer player;
    private Uri videoUri;

    public VideoPlayer(Context context) {
        super(context);
        initComponents(context);
    }

    public VideoPlayer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents(context);
    }

    public VideoPlayer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents(context);
    }

    public VideoPlayer(Context context, LayoutParams layoutParams){
        super(context);
        setLayoutParams(layoutParams);
        initComponents(context);
    }

    public VideoPlayer(Context context, int width, int height){
        super(context);
        LayoutParams params = new LayoutParams(width, height);
        setLayoutParams(params);
        initComponents(context);
    }

    private void initComponents(Context context){
        player = new ExoPlayer.Builder(context).build();
        setPlayer(player);
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

    public ExoPlayer getPlayer() {
        return player;
    }

}
