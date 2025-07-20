package com.realeyez.trademart.gui.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.R;
import com.realeyez.trademart.feed.FeedItem;
import com.realeyez.trademart.feed.FeedType;
import com.realeyez.trademart.gui.components.feed.FeedView;
import com.realeyez.trademart.gui.components.scroll.SnapScroll;
import com.realeyez.trademart.gui.dialogs.LoadingDialog;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

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
    ArrayList<FeedItem> feedItems;

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
        feedItems = new ArrayList<>();

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            fetchFeed();
            getActivity().runOnUiThread(() -> {
                for (FeedItem feedItem : feedItems) {
                    addFeedView(feedItem);
                }
            });
        });
    }

    private void addFeedView(FeedItem feed){
        Logger.log("adding feed...", LogLevel.INFO);
        LayoutParams params = new LayoutParams(
                scrollView.getWidth(),
                scrollView.getHeight());
        FeedView feedView = FeedView.inflate(requireActivity(), params, feed);
        assert feedView != null : "feedView should not be null";

        feedViews.add(feedView);
        contentPanel.addView(feedView);
    }

    private JSONArray feedsToJSON() throws JSONException {
        JSONArray arr = new JSONArray();
        for (FeedItem feedItem : feedItems)
            arr.put(new JSONObject()
                    .put("id", feedItem.getId())
                    .put("type", feedItem.getType().toString()));
        return arr;
    }

    private void fetchFeed(){
        LoadingDialog dialog = new LoadingDialog(requireActivity());
        getActivity().runOnUiThread(() -> dialog.show());
        try {
            JSONObject json = new JSONObject()
                .put("user_id", ResourceRepository.getResources().getCurrentUser().getId())
                .put("loaded_feeds", feedsToJSON());
            Response response = RequestUtil.sendPostRequest("/feed",
                    new Content.ContentBuilder().parseJson(json.toString()));
            JSONObject responseJson = response.getContentJson();
            Logger.log(responseJson.toString(), LogLevel.INFO);
            if(responseJson.getString("status").equals("failed")){
                String errMessage = responseJson.getString("message");
                requireActivity().runOnUiThread(() -> {
                    dialog.close();
                    Dialogs.showErrorDialog(errMessage, requireContext());
                });
                return;
            }
            JSONArray feedsJson = responseJson.getJSONObject("data").getJSONArray("feeds");
            for (int i = 0; i < feedsJson.length(); i++) {
                JSONObject feedJson = feedsJson.getJSONObject(i);
                FeedItem.Builder builder = new FeedItem.Builder();
                JSONArray mediaIdsJson = feedJson.getJSONArray("media_ids");
                ArrayList<Integer> mediaIds = new ArrayList<>();
                for (int j = 0; j < mediaIdsJson.length(); j++)
                    mediaIds.add(mediaIdsJson.getInt(j));

                feedItems.add(builder
                    .setId(feedJson.getInt("id"))
                    .setTitle(feedJson.getString("title"))
                    .setType(FeedType.parse(feedJson.getString("type")))
                    .setOwnerId(feedJson.getInt("owner_id"))
                    .setUsername(feedJson.getString("username"))
                    .setLikes(feedJson.getInt("likes"))
                    .setMediaIds(mediaIds)
                    .build());
            }
            requireActivity().runOnUiThread(() -> dialog.close());
        } catch (JSONException e){
            e.printStackTrace();
            requireActivity().runOnUiThread(() -> dialog.close());
        } catch (IOException e){
            e.printStackTrace();
            requireActivity().runOnUiThread(() -> dialog.close());
        }
    }

}
