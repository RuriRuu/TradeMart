package com.realeyez.trademart.job;

import android.net.Uri;

public class JobItemMixed extends JobItem {

    private String username;
    private String title;
    private Uri profilePictureUri;
    private JobTransactionType type;
    
    public JobItemMixed(JobTransactionType type, String username, String title, Uri profilePictureUri){
        super(username, title, profilePictureUri);
        this.username = username;
        this.title = title;
        this.profilePictureUri = profilePictureUri;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }

    public JobTransactionType getType() {
        return type;
    }

}
