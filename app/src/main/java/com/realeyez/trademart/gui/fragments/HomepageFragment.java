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
import com.realeyez.trademart.media.MediaType;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomepageFragment extends Fragment {

    private ScrollView scrollView;
    private SnapScroll snapScroll;
    private LinearLayout contentPanel;
    private SwipeRefreshLayout refreshLayout;

    ArrayList<FeedView> feedViews;
    ArrayList<FeedItem> feedItems;

    public HomepageFragment(){
        super(R.layout.fragment_homepage);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        feedViews = new ArrayList<>();
        feedItems = new ArrayList<>();


    }

    public void scrollToTop(){
        scrollView.smoothScrollTo(0, 0);
        snapScroll.resetChildren();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_homepage, container, false);

        scrollView = layout.findViewById(R.id.homepage_scroll);
        contentPanel = layout.findViewById(R.id.homepage_scroll_panel);
        refreshLayout = layout.findViewById(R.id.homepage_refresh);

        snapScroll = new SnapScroll(scrollView);

        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // scrollView.post(() -> {
        //     scrollView.scrollTo(0, 0);
        // });

        refreshLayout.setOnRefreshListener(() -> {
            refreshAction();
        });

        snapScroll.setOnChangeChildListener((lastChild, curChild) -> {
            onChangeChildAction(lastChild, curChild);
        });


        return layout;
    }

    @Override
    public void onResume(){
        super.onResume();
        snapScroll.refreshScroll();
        if(!feedViews.isEmpty()){
            feedViews.get(snapScroll.getCurChild()).preparePlayers();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        unfocus();
    }

    public void unfocus(){
        if(!feedViews.isEmpty()){
            feedViews.get(snapScroll.getCurChild()).destroyPlayers();
        }
    }

    private void onChangeChildAction(int lastChild, int curChild){
        feedViews.get(lastChild).destroyPlayers();
        feedViews.get(curChild).preparePlayers();
        if(curChild >= feedItems.size()-3){
            if(feedItems.size() <= 30){
                // snapScroll.setCurChild(curChild);
                loadFeed(false);
            }
        }
    }

    private void loadFeed(boolean clear){
        Activity activity = requireActivity();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            fetchFeed(clear);
            activity.runOnUiThread(() -> {
                for (FeedItem feedItem : feedItems) {
                    addFeedView(activity, feedItem);
                }
                Logger.log("this is insulting, please", LogLevel.INFO);
                ((FeedView) contentPanel.getChildAt(0)).preparePlayers();
            });
        });
    }

    private void refreshAction(){
        resetContent();
        refreshLayout.setRefreshing(false);
    }

    private void resetContent(){
        contentPanel.removeAllViews();
        snapScroll.resetChildren();
        snapScroll.setCurChild(0);
        loadFeed(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoadingDialog dialog = new LoadingDialog(requireActivity());
        dialog.show();
        Activity activity = requireActivity();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            fetchFeed(false);
            activity.runOnUiThread(() -> {
                for (int i = 0; i < feedItems.size(); i++) {
                    addFeedView(activity, feedItems.get(i));
                    if(i == 0){
                        feedViews.get(i).preparePlayers();
                    }
                }
                dialog.close();
            });
        });
    }

    private void addFeedView(Activity activity, FeedItem feed){
        Logger.log("adding feed...", LogLevel.INFO);
        LayoutParams params = new LayoutParams(
                scrollView.getWidth(),
                scrollView.getHeight());
        FeedView feedView = FeedView.inflate(activity, params, feed, feedViews.size() == 0);
        assert feedView != null : "feedView should not be null";

        feedViews.add(feedView);
        contentPanel.addView(feedView);
    }

    private JSONArray feedsToJSON() throws JSONException {
        JSONArray arr = new JSONArray();
            Logger.log("feedItems length: " +  feedItems.size(), LogLevel.INFO);
        for (FeedItem feedItem : feedItems) {
            Logger.log("adding loaded feed...", LogLevel.INFO);
            arr.put(new JSONObject()
                    .put("id", feedItem.getId())
                    .put("type", feedItem.getType().toString()));
        }
        return arr;
    }

    private void fetchFeed(boolean clear){
        // LoadingDialog dialog = new LoadingDialog(requireActivity());
        // getActivity().runOnUiThread(() -> dialog.show());
        try {
            JSONObject json = new JSONObject()
                .put("user_id", ResourceRepository.getResources().getCurrentUser().getId())
                .put("loaded_feeds", feedsToJSON());
            if(clear){
                feedItems.clear();
                feedViews.clear();
            }
            Logger.log(json.toString(), LogLevel.INFO);
            Response response = RequestUtil.sendPostRequest("/feed",
                    new Content.ContentBuilder().parseJson(json.toString()));
            JSONObject responseJson = response.getContentJson();
            Logger.log(responseJson.toString(), LogLevel.INFO);
            if(responseJson.getString("status").equals("failed")){
                String errMessage = responseJson.getString("message");
                requireActivity().runOnUiThread(() -> {
                    // dialog.close();
                    Dialogs.showErrorDialog(errMessage, requireContext());
                });
                return;
            }
            JSONArray feedsJson = responseJson.getJSONObject("data").getJSONArray("feeds");
            for (int i = 0; i < feedsJson.length(); i++) {
                Logger.log("FEEDS INIT #" + i, LogLevel.INFO);
                JSONObject feedJson = feedsJson.getJSONObject(i);
                FeedItem.Builder builder = new FeedItem.Builder();
                JSONArray mediaIdsJson = feedJson.getJSONArray("media_ids");
                ArrayList<Integer> mediaIds = new ArrayList<>();
                for (int j = 0; j < mediaIdsJson.length(); j++){
                    Logger.log("adding media id on: " + j, LogLevel.INFO);
                    mediaIds.add(mediaIdsJson.getInt(j));
                }

                JSONArray mediaTypesJson = feedJson.getJSONArray("media_types");
                ArrayList<MediaType> mediaTypes = new ArrayList<>();
                for (int j = 0; j < mediaTypesJson.length(); j++){
                    Logger.log("adding media type on: " + j, LogLevel.INFO);
                    mediaTypes.add(MediaType.valueOf(mediaTypesJson.getString(j)));
                }

                feedItems.add(builder
                    .setId(feedJson.getInt("id"))
                    .setTitle(feedJson.getString("title"))
                    .setType(FeedType.parse(feedJson.getString("type")))
                    .setOwnerId(feedJson.getInt("owner_id"))
                    .setUsername(feedJson.getString("username"))
                    .setLikes(feedJson.getInt("likes"))
                    .setUserLiked(feedJson.getBoolean("user_liked"))
                    .setMediaIds(mediaIds)
                    .setMediaTypes(mediaTypes)
                    .build());
            }
            // requireActivity().runOnUiThread(() -> dialog.close());
        } catch (JSONException e){
            e.printStackTrace();
            // requireActivity().runOnUiThread(() -> dialog.close());
        } catch (IOException e){
            e.printStackTrace();
            // requireActivity().runOnUiThread(() -> dialog.close());
        }
    }

}
