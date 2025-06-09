package com.realeyez.trademart.gui.components.createpost;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImagePanel {

    private Context context;
    private LinearLayout images_panel;
    private LinearLayout image_panel_layout;
    private ImageView imageView;
    private FrameLayout panelFrame;
    private Button removeButton;
    private Uri imageUri;
    private ArrayList<ImagePanel> imagePanels;

    public ImagePanel(Context context, LinearLayout images_panel, Uri imageUri, ArrayList<ImagePanel> imagePanels){
        this.context = context;
        this.images_panel = images_panel;
        this.imageUri = imageUri;
        this.imagePanels = imagePanels;
        initComponents();
    }

    private void initComponents(){
        image_panel_layout = new LinearLayout(context);
        image_panel_layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                100,
                1.0f
                );
        image_panel_layout.setLayoutParams(linear_params);

        imageView = new ImageView(context);
        imageView.setImageURI(imageUri);
        LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        image_params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(image_params);

        panelFrame = new FrameLayout(context);
        initFrame(panelFrame);

        removeButton = new Button(context);
        removeButton.setText("X");
        LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                0.75f
                );
        btn_params.gravity = Gravity.CENTER_VERTICAL;
        removeButton.setLayoutParams(btn_params);

        setButtonOnClickListener();

        image_panel_layout.setWeightSum(1.0f);
        panelFrame.addView(imageView);
        image_panel_layout.addView(panelFrame);
        image_panel_layout.addView(removeButton);
    }

    private void initFrame(FrameLayout frame){
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                 LayoutParams.MATCH_PARENT,
                 LayoutParams.WRAP_CONTENT,
                 0.25f
                 );
         frame.setLayoutParams(params);
    }

    private void setButtonOnClickListener(){
        removeButton.setOnClickListener(view -> {
            images_panel.removeView(image_panel_layout);
            imagePanels.remove(this);
        });
    }

    public LinearLayout getLayout(){
        return image_panel_layout;
    }

    public Uri getImageUri() {
        return imageUri;
    }

}
