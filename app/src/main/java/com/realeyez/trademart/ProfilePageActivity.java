package com.realeyez.trademart;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePageActivity extends AppCompatActivity {

    FloatingActionButton newPostButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        initComponents();
    }

    private void initComponents(){
        newPostButton = findViewById(R.id.profile_post_fab);
        addOnClickListeners();
    }

    private void newPostButtonAction(View view){
        Intent explicitActivity = new Intent(ProfilePageActivity.this, CreatePostActivity.class);
        startActivity(explicitActivity);
    }

    private void addOnClickListeners(){
        newPostButton.setOnClickListener(view -> {
            newPostButtonAction(view);
        });
    }

}
