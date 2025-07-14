package com.realeyez.trademart;

import java.util.ArrayList;

import com.realeyez.trademart.gui.components.feed.FeedView;
import com.realeyez.trademart.gui.components.scroll.SnapScroll;
import com.realeyez.trademart.gui.sheets.HomepageCreateSheet;
import com.realeyez.trademart.resource.ResourceRepository;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

public class HomepageActivity extends AppCompatActivity {

    private ScrollView feedScroll;
    private SnapScroll snapScroll;
    private LinearLayout scrollPanel;

    private ImageButton homeButton;
    private ImageButton createButton;
    private ImageButton profileButton;

    private ArrayList<FeedView> feedViews;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initComponents();
        loadPlaceholders();
    }

    private void initComponents(){
        feedScroll = findViewById(R.id.homepage_scroll);
        scrollPanel = findViewById(R.id.homepage_scroll_panel);

        homeButton = findViewById(R.id.homepage_home_button);
        createButton = findViewById(R.id.homepage_create_post_button);
        profileButton = findViewById(R.id.homepage_profile_button);

        feedViews = new ArrayList<>();
        snapScroll = new SnapScroll(feedScroll);

        addOnClickListeners();
    }

    private void loadPlaceholders(){
        feedScroll.post(() -> {
            addFeedView();
            addFeedView();
            addFeedView();
        });
    }

    private void displayCreateOptions(){
        HomepageCreateSheet bottomSheet = new HomepageCreateSheet();
        bottomSheet.show(getSupportFragmentManager(), HomepageCreateSheet.TAG);
    }

    private void addFeedView(){
        LayoutParams params = new LayoutParams(
                feedScroll.getWidth(),
                feedScroll.getHeight());
        FeedView feedView = FeedView.inflate(this, params);
        assert feedView != null : "feedView should not be null";

        feedViews.add(feedView);
        scrollPanel.addView(feedView);
    }

    private void addOnClickListeners(){
        createButton.setOnClickListener(view -> {
            displayCreateOptions();
        });
        profileButton.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(HomepageActivity.this, ProfilePageActivity.class);
            explicitActivity.putExtra("user_id", ResourceRepository.getResources().getCurrentUser().getId());
            startActivity(explicitActivity);
        });
    }

}
