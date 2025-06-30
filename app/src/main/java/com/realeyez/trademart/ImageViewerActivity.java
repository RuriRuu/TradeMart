package com.realeyez.trademart;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ImageViewerActivity extends AppCompatActivity {

    private TextView nameView;
    private TextView titleView;

    private ImageView imageView;

    private ImageButton backButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initComponents();
    }

    @Override
    public void onResume(){
        super.onResume();
    }
    

    private void initComponents(){
         nameView = findViewById(R.id.imageviewer_name_view);
         titleView = findViewById(R.id.imageviewer_title_view);

         imageView = findViewById(R.id.imageviewer_imageview);

         backButton = findViewById(R.id.imageviewer_back_button);

         addOnClickListeners();
    }

    private void addOnClickListeners(){
        backButton.setOnClickListener(view -> {
            finish();
        });
    }

}
