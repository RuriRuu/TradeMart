package com.realeyez.trademart.job;

import android.net.Uri;

public class JobItem {

    private String username;
    private String title;
    private Uri profilePictureUri;
    
    public JobItem(String username, String title, Uri profilePictureUri){
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

}
