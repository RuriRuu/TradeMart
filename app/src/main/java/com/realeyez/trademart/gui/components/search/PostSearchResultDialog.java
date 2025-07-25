package com.realeyez.trademart.gui.components.search;

import com.realeyez.trademart.gui.components.search.event.OnSearchItemClickedListener;
import org.json.JSONException;

import com.realeyez.trademart.R;
import com.realeyez.trademart.search.MediaSearchResult;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostSearchResultDialog extends ConstraintLayout {

    private ImageView thumbnailView;
    private CircleImageView profilePicture;
    private TextView titleLabel;
    private TextView usernameLabel;

    private Uri profilePictureUri;
    private Uri thumbnailUri;
    private String username;
    private String title;

    private MediaSearchResult result;

    private OnSearchItemClickedListener onSearchItemClickedListener;


    public PostSearchResultDialog(@NonNull Context context) {
        super(context);
    }

    public PostSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PostSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PostSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        titleLabel = findViewById(R.id.postsearch_title);
        profilePicture = findViewById(R.id.postsearch_profile_picture);
        usernameLabel = findViewById(R.id.postsearch_username);
        thumbnailView = findViewById(R.id.postsearch_thumbnail_view);

        setOnClickListener(view -> {
            onSearchItemClickedListener.onSearchItemClicked(result);
        });
    }

    private void loadData(){
        if(profilePictureUri != null)
            profilePicture.setImageURI(profilePictureUri);
        else
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.profile_picture_placeholder, null));

        if(thumbnailUri != null)
            thumbnailView.setImageURI(thumbnailUri);

        usernameLabel.setText(username);
        titleLabel.setText(title);
    }

    public static PostSearchResultDialog inflate(LayoutInflater inflater, MediaSearchResult result) {
        PostSearchResultDialog layout = (PostSearchResultDialog) inflater.inflate(R.layout.post_search_result_dialog, null);
        try {
            layout.extractResult(result);
            layout.loadData();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return layout;
    }

    private void extractResult(MediaSearchResult result) throws JSONException {
        this.result = result;
        this.profilePictureUri = result.getProfilePictureUri();
        this.thumbnailUri = result.getThumbnailUri();
        this.title = result.getEntity().getString("title");
        this.username = result.getUser().getUsername();
    }

    public void setOnSearchItemClickedListener(OnSearchItemClickedListener listener){
        this.onSearchItemClickedListener = listener;
    }

}
