package com.realeyez.trademart;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.realeyez.trademart.gui.fragments.ConvosMenuFragment;
import com.realeyez.trademart.gui.fragments.HomepageFragment;
import com.realeyez.trademart.gui.fragments.JobTransactionsFragment;
import com.realeyez.trademart.gui.sheets.HomepageCreateSheet;
import com.realeyez.trademart.resource.ResourceRepository;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private ConvosMenuFragment convosFrag;
    private HomepageFragment homepageFrag;
    private JobTransactionsFragment jobsFrag;

    private BottomNavigationView bottomNav;

    private FragmentManager fragman;

    private int currentFrag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents(){
        convosFrag = new ConvosMenuFragment();
        homepageFrag = new HomepageFragment(); 
        jobsFrag = new JobTransactionsFragment();

        bottomNav = findViewById(R.id.main_nav_bar);

        fragman = getSupportFragmentManager();

        showHomepageFragment();

        addOnClickListeners();
    }

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

    private void showJobsFragment(){
        fragman.beginTransaction()
            .replace(R.id.main_content_panel, jobsFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void homeButtonAction(){
        if(currentFrag == 0){
            homepageFrag.scrollToTop();
        } else {
            showHomepageFragment();
        }
    }

    private void createButtonAction(){
        displayCreateOptions();
    }

    private void profileButtonAction(){
        Intent explicitActivity = new Intent(MainActivity.this, ProfilePageActivity.class);
        explicitActivity.putExtra("user_id", ResourceRepository.getResources().getCurrentUser().getId());
        startActivity(explicitActivity);
    }

    private void chatButtonAction(){
        showChatsFragment();
    }

    private void addOnClickListeners(){
        bottomNav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.main_action_home){
                homeButtonAction();
                currentFrag = 0;
                return true;
            }
            if(item.getItemId() == R.id.main_action_search){
                currentFrag = 1;
                homepageFrag.unfocus();
                // searchButtonAction();
                showJobsFragment();
                return true;
            }
            if(item.getItemId() == R.id.main_action_create) {
                createButtonAction();
                return false;
            }
            if(item.getItemId() == R.id.main_action_chats) {
                currentFrag = 2;
                homepageFrag.unfocus();
                chatButtonAction();
                return true;
            }
            if(item.getItemId() == R.id.main_action_profile) {
                homepageFrag.unfocus();
                profileButtonAction();
                return false;
            }
            return true;
        });
    }


}
