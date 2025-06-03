package com.realeyez.trademart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button ratings = findViewById(R.id.RatingCheck);
        ratings.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(MainActivity.this, RatingViewActivity.class);
            startActivity(explicitActivity);
        });

        Button interests = findViewById(R.id.InterestCheck);
        interests.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(MainActivity.this, InterestViewActivity.class);
            startActivity(explicitActivity);
        });

        Button buypage = findViewById(R.id.buypagebtn);
        buypage.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(MainActivity.this, JobListEditing.class);
            startActivity(explicitActivity);
        });

        Button skillcardpage = findViewById(R.id.skillcardbuttonview);
        skillcardpage.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(MainActivity.this, skill_card_view.class);
            startActivity(explicitActivity);
        });
    }
}