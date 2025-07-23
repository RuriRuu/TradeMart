package com.realeyez.trademart.gui.components.feed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.feed.FeedItem;
import com.realeyez.trademart.gui.components.scroll.SnapScrollH;
import com.realeyez.trademart.gui.media.VideoPlayer;
import com.realeyez.trademart.media.MediaType;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.ContentDisposition;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.Content.ContentBuilder;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.media3.common.Player;
import androidx.media3.ui.PlayerView;

public class FeedView extends ConstraintLayout {

    private HorizontalScrollView scrollView;
    private SnapScrollH snapScroll;

    private LinearLayout contentPanel;

    private ImageView userImage;
    private TextView titleField;
    private TextView authorField;
    private TextView likeCountField;

    private ImageButton likeButton;
    private ImageButton messageButton;

    private Activity activity;

    private FeedItem feed;

    private ArrayList<Uri> mediaUris;
    private boolean isFirst;

    public FeedView(Activity activity){
        super(activity);
        this.activity = activity;
    }

    public FeedView(Context context){
        super(context);
        activity = (Activity) context;
    }

    public FeedView(Context context, AttributeSet attrSet){
        super(context, attrSet);
        activity = (Activity) context;
    }

    public FeedView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        activity = (Activity) context;
    }

    private void initComponents(){
        scrollView = findViewById(R.id.feedview_content_scroll);
        contentPanel = findViewById(R.id.feedview_content_panel);

        userImage = findViewById(R.id.feedview_user_image);
        titleField = findViewById(R.id.feedview_title_field);
        authorField = findViewById(R.id.feedview_author_field);
        likeCountField = findViewById(R.id.feedview_like_count);
        likeButton = findViewById(R.id.feedview_like_button);
        messageButton = findViewById(R.id.feedview_message_button);

        // setOnFocusChangeListener((view, focused) -> {
        //     if(focused){
        //         preparePlayers();
        //     } else {
        //         destroyPlayers();
        //     }
        // });
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();

        initComponents();

        mediaUris = new ArrayList<>();
        snapScroll = new SnapScrollH(scrollView);

        addOnClickListeners();
    }

    public void destroyPlayers(){
        for (int i = 0; i < contentPanel.getChildCount(); i++) {
            if(contentPanel.getChildAt(i) instanceof PlayerView){
                Player player = ((PlayerView) contentPanel.getChildAt(i)).getPlayer();
                player.stop();
                player.clearMediaItems();
            }
        }
    }
            
    public void pausePlayers(){
        for (int i = 0; i < contentPanel.getChildCount(); i++) {
            if(contentPanel.getChildAt(i) instanceof PlayerView){
                Player player = ((PlayerView) contentPanel.getChildAt(i)).getPlayer();
                if(player.isPlaying()){
                    player.pause();
                }
            }
        }
    }

    public void preparePlayers(){
        for (int i = 0; i < contentPanel.getChildCount(); i++) {
            if(contentPanel.getChildAt(i) instanceof VideoPlayer){
                VideoPlayer player = (VideoPlayer) contentPanel.getChildAt(i);
                player.setMediaUri(mediaUris.get(i));
                player.getPlayer().setPlayWhenReady(true);
                player.prepare();
                // player.play();
            }
            
        }
    }

    private void loadFeed(){
        loadFeedDetails();
        addMediaPanels();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            requestProfilePicture();
        });
    }

    private void loadFeedDetails(){
        titleField.setText(feed.getTitle());
        authorField.setText(feed.getUsername());
        if(feed.userLiked()){
            setLikeButtonLiked(feed.getLikes());
        }
        likeCountField.setText(likeCountString(feed.getLikes()));
    }

    private String likeCountString(int likes){
        String out = new StringBuilder()
            .append(likes)
            .toString();
        return out;
    }

    private void requestProfilePicture(){
        try {
            String path = new StringBuilder()
                .append("/user/")
                .append(feed.getOwnerId())
                .append("/avatar").toString();
            Response response = RequestUtil.sendGetRequest(path);
            String filename = response.getContentDisposition().getField("filename");
            CacheFile cacheFile = CacheFile.cache(activity.getCacheDir(), filename, response.getContentBytes());
            File file = cacheFile.getFile();
            activity.runOnUiThread(() -> {
                userImage.setImageURI(Uri.fromFile(file));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLikeButtonLiked(int likeCount){
        likeButton.setImageDrawable(getResources().getDrawable(R.drawable.heart_filled, null));
        likeCountField.setText(likeCountString(likeCount));
        likeButton.setEnabled(false);
    }

    private void addMediaPanels(){
        ArrayList<MediaType> types = feed.getMediaTypes();
        for (int i = 0; i < feed.getMediaIds().size(); i++) {
            addPanels(types, i);
        }
            
    }

    private void addPanels(ArrayList<MediaType> types, int i){
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            Uri uri = fetchMediaUri(feed.getMediaIds().get(i), types.get(i));
            activity.runOnUiThread(() -> {
                switch (types.get(i)) {
                    case IMAGE:
                        addImagePanel(uri);
                        break;
                    case VIDEO:
                        addVideoPanel(uri);
                        break;
                }
            });
        });
    }

    private Uri fetchMediaUri(int mediaId, MediaType type){
        Uri uri = null;
        String path = new StringBuilder()
            .append("/media/")
            .append(mediaId).toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            ContentDisposition disposition = response.getContentDisposition();
            String filename = disposition.getField("filename");
            if(type == MediaType.VIDEO){
                String location = new StringBuilder()
                    .append(response.getHost())
                    .append(response.getLocation())
                    .toString();
                uri = Uri.parse(location);
            } else {
                CacheFile cacheFile = CacheFile.cache(getContext().getCacheDir(), filename, response.getContentBytes());
                File file = cacheFile.getFile();
                uri = Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    private void addImagePanel(Uri uri){
        ImageView imageView = new ImageView(getContext());
        layoutMediaPanel(imageView);
        imageView.setImageURI(uri);
        mediaUris.add(uri);
        contentPanel.addView(imageView);
    }

    private void addVideoPanel(Uri uri){
        VideoPlayer player = new VideoPlayer(getContext());
        layoutMediaPanel(player);
        player.setMediaUri(uri);
        player.getPlayer().setPlayWhenReady(true);
        if(isFirst){
            player.prepare();
        }
        mediaUris.add(uri);
        contentPanel.addView(player);
    }

    private void layoutMediaPanel(View view){
        android.widget.FrameLayout.LayoutParams params =  new android.widget.FrameLayout.LayoutParams(
                scrollView.getWidth(),
                scrollView.getHeight(),
                Gravity.CENTER);
        view.setLayoutParams(params);
    }

    public void setFeed(FeedItem feed){
        this.feed = feed;
    }

    public void likeClickAction(){
        Content data = new Content.ContentBuilder()
            .put("id", feed.getId())
            .put("type", feed.getType().toString())
            .build();
        Content content = new Content.ContentBuilder()
            .put("user_id", ResourceRepository.getResources().getCurrentUser().getId())
            .put("data", data)
            .build();
        try {
            Response response = RequestUtil.sendPostRequest("/feed/like", content);
            JSONObject responseJson = response.getContentJson();
            if(responseJson.getString("status").equals("failed")){
                String rMessage = responseJson.getString("message");
                activity.runOnUiThread(() -> {
                    Dialogs.showErrorDialog(rMessage, getContext());
                });
                return;
            }
            JSONObject updated = responseJson.getJSONObject("data").getJSONObject("feed_updated");
            int likeCount = updated.getInt("likes");
            activity.runOnUiThread(() -> {
                setLikeButtonLiked(likeCount);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void messageButtonAction(){
        int mateId = feed.getOwnerId();
        int userId = ResourceRepository.getResources().getCurrentUser().getId();
        int convoId = -1;

        Content content = new ContentBuilder()
            .put("user1_id", userId)
            .put("user2_id", mateId)
            .build();

        try {
            Response response = RequestUtil.sendPostRequest("/message/convos", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                activity.runOnUiThread(() -> {
                    Dialogs.showErrorDialog(rMessage, getContext());
                });
                return;
            }
            convoId = json.getJSONObject("data").getInt("convo_id");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(convoId == -1){
            return;
        }
        Intent intent = new Intent(getContext(), MessagingActivity.class);
        intent.putExtra("user_id", mateId);
        intent.putExtra("username", feed.getUsername());
        intent.putExtra("convo_id", convoId);
        getContext().startActivity(intent);
    }

    private void addOnClickListeners(){
        likeButton.setOnClickListener(view -> {
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(() -> {
                likeClickAction();
            });
        });
        messageButton.setOnClickListener(view -> {
            VideoPlayer player = (VideoPlayer) contentPanel.getChildAt(snapScroll.getCurChild());
            player.pause();
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(() -> {
                messageButtonAction();
            });
        });
    }

    public static FeedView inflate(Activity activity, LayoutParams params, FeedItem feed, boolean isFirst) {
        FeedView view = (FeedView) activity.getLayoutInflater()
                .inflate(R.layout.layout_feed_view, null);
        view.isFirst = isFirst;
        view.setLayoutParams(params);
        view.setFeed(feed);
        view.loadFeed();
        return view;
    }

}
