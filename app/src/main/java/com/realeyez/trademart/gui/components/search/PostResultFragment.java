package com.realeyez.trademart.gui.components.search;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.realeyez.trademart.PostViewerActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.search.MediaSearchResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PostResultFragment extends Fragment {

    private ScrollView scrollView;
    private LinearLayout resultPanel;
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<MediaSearchResult> results;
    private Runnable onRefreshEvent;

    public PostResultFragment(){
         super(R.layout.fragment_searchresult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_searchresult, container, false);

        scrollView = layout.findViewById(R.id.searchresult_scroll);
        resultPanel = layout.findViewById(R.id.searchresult_scroll_panel);
        refreshLayout = layout.findViewById(R.id.searchresult_refresh);

        refreshLayout.setOnRefreshListener(() -> {
            if(onRefreshEvent != null)
                onRefreshEvent.run();
            refreshLayout.setRefreshing(false);
        });

        results = new ArrayList<>();

        return layout;
    }

    public void loadResults(ArrayList<MediaSearchResult> results){
        resultPanel.removeAllViews();
        this.results.clear();

        Activity activity = requireActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            if(results == null || results.size() == 0){
                return;
            }
            for (MediaSearchResult result : results) {
                // BUG: activity is null bug
                activity.runOnUiThread(() -> {
                    this.results.add(result);
                    addPostResultPanels(result);
                });
            }
        });
    }

    private void addPostResultPanels(MediaSearchResult result){
        PostSearchResultDialog panel = PostSearchResultDialog.inflate(getLayoutInflater(), result);
        panel.setOnSearchItemClickedListener(searchResult -> {
            Intent intent = new Intent(getContext(), PostViewerActivity.class);
            intent.putExtra("post_id", result.getId());
            intent.putExtra("media_ids", result.getMediaIds());
            intent.putExtra("username", result.getUser().getUsername());
            startActivity(intent);
        });
        resultPanel.addView(panel);
    }

    public void setOnRefreshEvent(Runnable onRefreshEvent){
        this.onRefreshEvent = onRefreshEvent;
    }
    
}
