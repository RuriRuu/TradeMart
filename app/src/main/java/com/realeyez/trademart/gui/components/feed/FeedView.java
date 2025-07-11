package com.realeyez.trademart.gui.components.feed;

import com.realeyez.trademart.R;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class FeedView extends ConstraintLayout {

    private PlayerView playerView;
    private ExoPlayer player;
    private Uri videoUri;

    private ImageView userImage;
    private TextView titleField;
    private TextView authorField;

    private ImageButton likeButton;
    private ImageButton messageButton;

    private Activity activity;

    public FeedView(Activity activity){
        super(activity);
        this.activity = activity;
        initComponents();
    }

    public FeedView(Context context){
        super(context);
        activity = (Activity) context;
        initComponents();
    }

    public FeedView(Context context, AttributeSet attrSet){
        super(context, attrSet);
        activity = (Activity) context;
        initComponents();
    }


    private void initComponents(){
        playerView = activity.findViewById(R.id.feedview_video_view);
        userImage = activity.findViewById(R.id.feedview_user_image);
        titleField = activity.findViewById(R.id.feedview_title_field);
        authorField = activity.findViewById(R.id.feedview_author_field);
        likeButton = activity.findViewById(R.id.feedview_like_button);
        messageButton = activity.findViewById(R.id.feedview_message_button);
    }

    public void setupPlayer(){
        player.setMediaItem(MediaItem.fromUri(videoUri));
        player.prepare();
    }

    public void play(){
        player.play();
    }

    public void pause(){
        player.pause();
    }

    public static FeedView inflate(Activity activity) {
        return (FeedView) activity.getLayoutInflater()
                .inflate(R.layout.layout_feed_view, null);
    }

    public static FeedView inflate(Activity activity, LayoutParams params) {
        FeedView view = (FeedView) activity.getLayoutInflater()
                .inflate(R.layout.layout_feed_view, null);
        view.setLayoutParams(params);
        return view;
    }


}
