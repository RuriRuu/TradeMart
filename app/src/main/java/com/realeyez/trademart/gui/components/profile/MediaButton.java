package com.realeyez.trademart.gui.components.profile;

import java.util.ArrayList;

import com.realeyez.trademart.PostViewerActivity;
import com.realeyez.trademart.post.PostData;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

public class MediaButton {

    private Context context;
    private ImageView imageView;
    private PostData postData;
    private ArrayList<Integer> mediaIds;
    private int postId;
    
    public MediaButton(Context context, ImageView imageView, PostData postData){
        this.context = context;
        this.imageView = imageView;
        this.postId = postData.getPostId();
        this.mediaIds = postData.getMediaIds();
        setOnClickEvents();
    }
    
    public ArrayList<Integer> getMediaIds() {
        return mediaIds;
    }

    public ImageView getImageView() {
        return imageView;
    }

    private void setOnClickEvents(){
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostViewerActivity.class);
            intent.putExtra("post_id", postId);
            intent.putExtra("media_ids", mediaIds);
            context.startActivity(intent);
        });
    }


}
