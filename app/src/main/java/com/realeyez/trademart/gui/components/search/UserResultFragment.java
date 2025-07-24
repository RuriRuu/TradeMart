package com.realeyez.trademart.gui.components.search;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;

import com.realeyez.trademart.R;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.search.SearchResult;
import com.realeyez.trademart.search.UserSearchResult;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Activity;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

public class UserResultFragment extends Fragment {

    private ScrollView scrollView;
    private LinearLayout resultPanel;
    private SwipeRefreshLayout refreshLayout;
    private TextView temp;

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
            onRefreshEvent.run();
            refreshLayout.setRefreshing(false);
        });

        results = new ArrayList<>();

        return layout;
    }

    public void loadResults(ArrayList<SearchResult> results){
        Logger.log("I'm in" , LogLevel.INFO);
        resultPanel.removeAllViews();
        this.results.clear();

        Activity activity = getActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            if(results == null || results.size() == 0){
                Logger.log("empty search set", LogLevel.INFO);
                return;
            }
            Logger.log("Locking in :|" , LogLevel.INFO);
            for (int i = 0; i < results.size(); i++) {
                Uri pfpUri = requestProfilePicture(results.get(i));
                UserSearchResult userResult = new UserSearchResult(results.get(i), pfpUri);
                activity.runOnUiThread(() -> {
                    Logger.log("Adding panels..." , LogLevel.INFO);
                    this.results.add(userResult);
                    addUserResultPanels(userResult);
                });
            }
        });
    }

    private void addUserResultPanels(UserSearchResult result){
        UserSearchResultDialog panel = UserSearchResultDialog.inflate(getLayoutInflater(), result);
        panel.setOnSearchItemClickedListener(searchResult -> {
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
