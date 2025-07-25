package com.realeyez.trademart.gui.components.search;

import com.realeyez.trademart.gui.components.search.event.OnSearchItemClickedListener;
import org.json.JSONException;

import com.realeyez.trademart.R;
import com.realeyez.trademart.search.MediaSearchResult;

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

public class JobListingSearchResultDialog extends ConstraintLayout {

    private ImageView thumbnailView;
    private CircleImageView profilePicture;
    private TextView amountLabel;
    private TextView titleLabel;
    private TextView usernameLabel;

    private Uri profilePictureUri;
    private Uri thumbnailUri;
    private String username;
    private String title;
    private double price;

    private MediaSearchResult result;

    private OnSearchItemClickedListener onSearchItemClickedListener;


    public JobListingSearchResultDialog(@NonNull Context context) {
        super(context);
    }

    public JobListingSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JobListingSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public JobListingSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        titleLabel = findViewById(R.id.jobsearch_title);
        profilePicture = findViewById(R.id.jobsearch_profile_picture);
        usernameLabel = findViewById(R.id.jobsearch_username);
        thumbnailView = findViewById(R.id.jobsearch_thumbnail_view);
        amountLabel = findViewById(R.id.jobsearch_amount);

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

        amountLabel.setText(generatePriceString());
        usernameLabel.setText(username);
        titleLabel.setText(title);
    }

    private String generatePriceString(){
        String text = new StringBuilder()
            .append(String.format("₱ %.2f", price))
            .toString();
        return text;
    }

    public static JobListingSearchResultDialog inflate(LayoutInflater inflater, MediaSearchResult result) {
        JobListingSearchResultDialog layout = (JobListingSearchResultDialog) inflater.inflate(R.layout.job_search_result_dialog, null);
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
        this.title = result.getEntity().getString("job_title");
        this.price = result.getEntity().getDouble("amount");
        this.username = result.getUser().getUsername();
    }

    public void setOnSearchItemClickedListener(OnSearchItemClickedListener listener){
        this.onSearchItemClickedListener = listener;
    }

}
