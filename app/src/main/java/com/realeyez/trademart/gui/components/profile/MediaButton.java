package com.realeyez.trademart.gui.components.profile;

import com.realeyez.trademart.PostViewerActivity;
import com.realeyez.trademart.post.PostData;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

public class MediaButton {

    private Context context;
    private ImageView imageView;
    private PostData postData;
    
    public MediaButton(Context context, ImageView imageView, PostData postData){
        this.context = context;
        this.imageView = imageView;
        this.postData = postData;
        setOnClickEvents();
    }
    
    public ImageView getImageView() {
        return imageView;
    }

    // TODO: load the PostViewer media here then send it through something like a parcel maybe.
    private void setOnClickEvents(){
        imageView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PostViewerActivity.class);
            intent.putExtra("post_id", postData.getPostId());
            intent.putExtra("media_ids", postData.getMediaIds());
            intent.putExtra("username", postData.getUsername());
            context.startActivity(intent);
        });
    }


}
