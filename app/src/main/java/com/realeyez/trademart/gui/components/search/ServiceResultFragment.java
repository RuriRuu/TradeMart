package com.realeyez.trademart.gui.components.search;

import com.realeyez.trademart.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ServiceResultFragment extends Fragment {

    private ScrollView scrollView;
    private LinearLayout resultPanel;
    private SwipeRefreshLayout refreshLayout;
    private TextView temp;

    public ServiceResultFragment(){
         super(R.layout.fragment_searchresult);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_searchresult, container, false);

        scrollView = layout.findViewById(R.id.searchresult_scroll);
        resultPanel = layout.findViewById(R.id.searchresult_scroll_panel);
        refreshLayout = layout.findViewById(R.id.searchresult_refresh);

        refreshLayout.setOnRefreshListener(() -> {
            refreshAction();
        });

        temp = new TextView(layout.getContext());

        temp.setText("Service");
        resultPanel.addView(temp);

        return layout;
    }

    // TODO: refresh action
    private void refreshAction(){
    }
    
}
