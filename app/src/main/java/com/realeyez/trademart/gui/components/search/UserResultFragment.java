package com.realeyez.trademart.gui.components.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import com.realeyez.trademart.ProfilePageActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.search.SearchResult;
import com.realeyez.trademart.search.UserSearchResult;
import com.realeyez.trademart.util.CacheFile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class UserResultFragment extends Fragment {

    private ScrollView scrollView;
    private LinearLayout resultPanel;
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<UserSearchResult> results;
    private Runnable onRefreshEvent;

    public UserResultFragment(){
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

    public void loadResults(ArrayList<UserSearchResult> results){
        if(this.results == null || resultPanel == null){
            return;
        }
        resultPanel.removeAllViews();
        this.results.clear();

        Activity activity = getActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            if(results == null || results.size() == 0){
                return;
            }
            for (UserSearchResult result : results) {
                activity.runOnUiThread(() -> {
                    this.results.add(result);
                    addUserResultPanels(result);
                });
            }
        });
    }

    private void addUserResultPanels(UserSearchResult result){
        UserSearchResultDialog panel = UserSearchResultDialog.inflate(getLayoutInflater(), result);
        panel.setOnSearchItemClickedListener(searchResult -> {
            int userId = result.getId();
            Intent intent = new Intent(getContext(), ProfilePageActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
        });
        resultPanel.addView(panel);
    }

    private Uri requestProfilePicture(SearchResult result){
        try {
            String path = new StringBuilder()
                .append("/user/")
                .append(result.getEntity().getInt("id"))
                .append("/avatar")
                .toString();
            Response response = RequestUtil.sendGetRequest(path);
            String filename = response.getContentDispositionField("filename");
            byte[] profilePictureData = response.getContentBytes();

            CacheFile cache = CacheFile.cache(getContext().getCacheDir(), filename, profilePictureData);

            return Uri.fromFile(cache.getFile());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setOnRefreshEvent(Runnable onRefreshEvent){
        this.onRefreshEvent = onRefreshEvent;
    }
    
}
