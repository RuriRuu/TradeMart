package com.realeyez.trademart.gui.components.search;

import com.realeyez.trademart.gui.components.search.event.OnSearchItemClickedListener;
import org.json.JSONException;

import com.realeyez.trademart.R;
import com.realeyez.trademart.search.UserSearchResult;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchResultDialog extends ConstraintLayout {

    private CircleImageView profilePicture;
    private TextView usernameLabel;

    private Uri profilePictureUri;
    private String username;

    private UserSearchResult result;

    private OnSearchItemClickedListener onSearchItemClickedListener;


    public UserSearchResultDialog(@NonNull Context context) {
        super(context);
    }

    public UserSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UserSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        profilePicture = findViewById(R.id.usersearch_result_image);
        usernameLabel = findViewById(R.id.usersearch_result_username);

        setOnClickListener(view -> {
            onSearchItemClickedListener.onSearchItemClicked(result);
        });

    }

    private void loadData(){
        if(profilePictureUri != null)
            profilePicture.setImageURI(profilePictureUri);
        else
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.profile_picture_placeholder, null));
        usernameLabel.setText(username);
    }

    public static UserSearchResultDialog inflate(LayoutInflater inflater, UserSearchResult result) {
        UserSearchResultDialog layout = (UserSearchResultDialog) inflater.inflate(R.layout.user_search_result_dialog, null);
        try {
            layout.extractResult(result);
            layout.loadData();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return layout;
    }

    private void extractResult(UserSearchResult result) throws JSONException {
        this.result = result;
        this.profilePictureUri = result.getProfilePictureUri();
        this.username = result.getEntity()
            .getString("username");
    }

    public void setOnSearchItemClickedListener(OnSearchItemClickedListener listener){
        this.onSearchItemClickedListener = listener;
    }

}
