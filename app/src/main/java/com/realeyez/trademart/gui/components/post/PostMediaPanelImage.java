package com.realeyez.trademart.gui.components.post;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PostMediaPanelImage implements PostMediaPanel {

    private Uri uri;
    private ImageView imageView;
    private Context context;

    public PostMediaPanelImage(Context context, LayoutParams params, Uri uri){
        this.uri = uri;
        this.context = context;
        initComponents(params);
    }

    private void initComponents(LayoutParams params){
        imageView = new ImageView(context);
        imageView.setScaleType(ScaleType.FIT_CENTER);
        imageView.setLayoutParams(params);
        imageView.setImageURI(uri);
    }

    @Override
    public Uri getUri(){
        return uri;
    }

    public ImageView getImageView() {
        return imageView;
    }
    
}
