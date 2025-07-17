package com.realeyez.trademart;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private BottomNavigationView bottomNav;
    private BottomNavigationItemView homeButton;
    private BottomNavigationItemView createButton;
    private BottomNavigationItemView chatButton;
    private BottomNavigationItemView profileButton;

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

        bottomNav = findViewById(R.id.homepage_nav_bar);

        homeButton = findViewById(R.id.homepage_action_home);
        createButton = findViewById(R.id.homepage_action_create);
        chatButton = findViewById(R.id.homepage_action_chats);
        profileButton = findViewById(R.id.homepage_action_profile);

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
            bottomNav.setSelectedItemId(R.id.homepage_action_home);
        });
        profileButton.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(HomepageActivity.this, ProfilePageActivity.class);
            explicitActivity.putExtra("user_id", ResourceRepository.getResources().getCurrentUser().getId());
            startActivity(explicitActivity);
            bottomNav.setSelectedItemId(R.id.homepage_action_home);
        });
    }


}
