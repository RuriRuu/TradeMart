package com.realeyez.trademart;

import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.realeyez.trademart.gui.components.feed.FeedView;
import com.realeyez.trademart.gui.fragments.ConvosMenuFragment;
import com.realeyez.trademart.gui.fragments.HomepageFragment;
import com.realeyez.trademart.gui.sheets.HomepageCreateSheet;
import com.realeyez.trademart.resource.ResourceRepository;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    // private ScrollView feedScroll;
    // private SnapScroll snapScroll;
    // private LinearLayout scrollPanel;

    private FrameLayout contentPanel;
    private ConvosMenuFragment convosFrag;
    private HomepageFragment homepageFrag;

    private BottomNavigationView bottomNav;

    // private BottomNavigationItemView homeButton;
    // private BottomNavigationItemView createButton;
    // private BottomNavigationItemView chatButton;
    // private BottomNavigationItemView profileButton;

    private FragmentManager fragman;

    private ArrayList<FeedView> feedViews;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        // loadPlaceholders();
    }

    private void initComponents(){
        // feedScroll = findViewById(R.id.main_scroll);
        // scrollPanel = findViewById(R.id.main_scroll_panel);

        convosFrag = new ConvosMenuFragment();
        homepageFrag = new HomepageFragment(); 

        contentPanel = findViewById(R.id.main_content_panel);
        bottomNav = findViewById(R.id.main_nav_bar);

        // homeButton = findViewById(R.id.main_action_home);
        // createButton = findViewById(R.id.main_action_create);
        // chatButton = findViewById(R.id.main_action_chats);
        // profileButton = findViewById(R.id.main_action_profile);
        //

        fragman = getSupportFragmentManager();

        feedViews = new ArrayList<>();
        // snapScroll = new SnapScroll(feedScroll);

        showHomepageFragment();

        addOnClickListeners();
    }

    // private void loadPlaceholders(){
    //     feedScroll.post(() -> {
    //         addFeedView();
    //         addFeedView();
    //         addFeedView();
    //     });
    // }

    private void displayCreateOptions(){
        HomepageCreateSheet bottomSheet = new HomepageCreateSheet();
        bottomSheet.show(getSupportFragmentManager(), HomepageCreateSheet.TAG);
    }

    private void showHomepageFragment(){
        fragman.beginTransaction()
            .replace(R.id.main_content_panel, homepageFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void showChatsFragment(){
        fragman.beginTransaction()
            .replace(R.id.main_content_panel, convosFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void homeButtonAction(){
        showHomepageFragment();
    }

    private void createButtonAction(){
        displayCreateOptions();
        bottomNav.setSelectedItemId(R.id.main_action_home);
    }

    private void profileButtonAction(){
        Intent explicitActivity = new Intent(MainActivity.this, ProfilePageActivity.class);
        explicitActivity.putExtra("user_id", ResourceRepository.getResources().getCurrentUser().getId());
        startActivity(explicitActivity);
        bottomNav.setSelectedItemId(R.id.main_action_home);
    }

    private void chatButtonAction(){
        showChatsFragment();
    }

    private void addOnClickListeners(){
        bottomNav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.main_action_home){
                homeButtonAction();
                return true;
            }
            if(item.getItemId() == R.id.main_action_search){
                return true;
            }
            if(item.getItemId() == R.id.main_action_create) {
                createButtonAction();
                return false;
            }
            if(item.getItemId() == R.id.main_action_chats) {
                chatButtonAction();
                return true;
            }
            if(item.getItemId() == R.id.main_action_profile) {
                profileButtonAction();
                return false;
            }
            return true;
        });
    }


}
