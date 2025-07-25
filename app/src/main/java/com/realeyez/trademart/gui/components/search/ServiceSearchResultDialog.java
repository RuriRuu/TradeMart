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

public class ServiceSearchResultDialog extends ConstraintLayout {

    private ImageView thumbnailView;
    private CircleImageView profilePicture;
    private TextView priceLabel;
    private TextView titleLabel;
    private TextView usernameLabel;

    private Uri profilePictureUri;
    private Uri thumbnailUri;
    private String username;
    private String title;
    private double price;

    private MediaSearchResult result;

    private OnSearchItemClickedListener onSearchItemClickedListener;


    public ServiceSearchResultDialog(@NonNull Context context) {
        super(context);
    }

    public ServiceSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ServiceSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ServiceSearchResultDialog(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        titleLabel = findViewById(R.id.servicesearch_title);
        profilePicture = findViewById(R.id.servicesearch_profile_picture);
        usernameLabel = findViewById(R.id.servicesearch_username);
        thumbnailView = findViewById(R.id.servicesearch_thumbnail_view);
        priceLabel = findViewById(R.id.servicesearch_price);

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

        priceLabel.setText(generatePriceString());
        usernameLabel.setText(username);
        titleLabel.setText(title);
    }

    private String generatePriceString(){
        String text = new StringBuilder()
            .append(String.format("₱ %.2f", price))
            .toString();
        return text;
    }

    public static ServiceSearchResultDialog inflate(LayoutInflater inflater, MediaSearchResult result) {
        ServiceSearchResultDialog layout = (ServiceSearchResultDialog) inflater.inflate(R.layout.service_search_result_dialog, null);
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
        Logger.log("service search result entity: " + result.getEntity().toString(), LogLevel.INFO);
        this.title = result.getEntity().getString("service_title");
        this.price = result.getEntity().getDouble("service_price");
        this.username = result.getUser().getUsername();
    }

    public void setOnSearchItemClickedListener(OnSearchItemClickedListener listener){
        this.onSearchItemClickedListener = listener;
    }

}
