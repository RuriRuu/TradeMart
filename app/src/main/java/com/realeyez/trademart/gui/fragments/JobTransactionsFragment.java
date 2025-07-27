package com.realeyez.trademart.gui.fragments;

import com.google.android.material.tabs.TabLayout;
import com.realeyez.trademart.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class JobTransactionsFragment extends Fragment {

    private TabLayout tabs;
    private FrameLayout contentPanel;

    private FragmentManager fragman;

    private ActiveTransactionsListFragment activeFrag;
    private ApplicationsListFragment appsFrag;
    private HiringsListFragment hiringFrag;
    private CompletedTransactionsListFragment completedFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_job_transactions, container, false);

        tabs = layout.findViewById(R.id.jobtransactions_tabs);
        contentPanel = layout.findViewById(R.id.jobtransactions_content_panel);

        activeFrag = new ActiveTransactionsListFragment();
        appsFrag = new ApplicationsListFragment();
        hiringFrag = new HiringsListFragment();
        completedFrag = new CompletedTransactionsListFragment();

        fragman = getParentFragmentManager();

        addListeners();

        showActivesFragment();

        return layout;
    }

    private void showActivesFragment(){
        fragman.beginTransaction()
            .replace(contentPanel.getId(), activeFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void showApplicationsFragment(){
        fragman.beginTransaction()
            .replace(contentPanel.getId(), appsFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void showHiringsFragment(){
        fragman.beginTransaction()
            .replace(contentPanel.getId(), hiringFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void showCompletedFragment(){
        fragman.beginTransaction()
            .replace(contentPanel.getId(), completedFrag)
            .setReorderingAllowed(true)
            .commit();
    }

    private void onTabSelectedAction(TabLayout.Tab tab) {
        if(tab.getPosition() == 0){
            showActivesFragment();
            return;
        }
        if(tab.getPosition() == 1){
            showApplicationsFragment();
            return;
        }
        if(tab.getPosition() == 2){
            showHiringsFragment();
            return;
        }
        if(tab.getPosition() == 3){
            showCompletedFragment();
            return;
        }
    }

    private void addListeners(){
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelectedAction(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

}
