package com.realeyez.trademart.gui.fragments;

import java.util.ArrayList;

import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.feed.FeedView;
import com.realeyez.trademart.gui.components.scroll.SnapScroll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.fragment.app.Fragment;

public class HomepageFragment extends Fragment {

    private ScrollView scrollView;
    private SnapScroll snapScroll;
    private LinearLayout contentPanel;

    ArrayList<FeedView> feedViews;

    public HomepageFragment(){
        super(R.layout.fragment_homepage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_homepage, container, false);

        scrollView = layout.findViewById(R.id.homepage_scroll);
        contentPanel = layout.findViewById(R.id.homepage_scroll_panel);

        snapScroll = new SnapScroll(scrollView);

        feedViews = new ArrayList<>();

        scrollView.post(() -> {
            addFeedView();
            addFeedView();
            addFeedView();
        });

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void addFeedView(){
        LayoutParams params = new LayoutParams(
                scrollView.getWidth(),
                scrollView.getHeight());
        FeedView feedView = FeedView.inflate(requireActivity(), params);
        assert feedView != null : "feedView should not be null";

        feedViews.add(feedView);
        contentPanel.addView(feedView);
    }

}
