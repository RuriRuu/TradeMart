package com.realeyez.trademart.search;

import com.realeyez.trademart.user.User;

import android.net.Uri;

import org.json.JSONObject;

import java.util.ArrayList;

public class MediaSearchResult extends SearchResult {

    private Uri thumbnailUri;
    private ArrayList<Integer> mediaIds;
    private User user;

    public MediaSearchResult(Builder builder){
        super(builder);
        user = builder.user;
        mediaIds = builder.mediaIds;
        thumbnailUri = builder.thumbnailUri;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Integer> getMediaIds() {
        return mediaIds;
    }

    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    public static class Builder extends SearchResult.Builder {

        private Uri thumbnailUri;
        private User user;
        private ArrayList<Integer> mediaIds;

        public Builder(){
            user = null;
            mediaIds = null;
            thumbnailUri = null;
        }

        public Builder setThumbnailUri(Uri thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
            return this;
        }

        @Override
        public Builder setId(int id) {
            return (Builder) super.setId(id);
        }

        @Override
        public Builder setTerm(String term) {
            return (Builder) super.setTerm(term);
        }

        @Override
        public Builder setRelPoints(double relPoints) {
            return (Builder) super.setRelPoints(relPoints);
        }

        @Override
        public Builder setEntity(JSONObject entity) {
            return (Builder) super.setEntity(entity);
        }

        public Builder setMediaIds(ArrayList<Integer> mediaIds) {
            this.mediaIds = mediaIds;
            return this;
        }

        @Override
        public Builder setProfilePictureUri(Uri profilePictureUri) {
            return (Builder) super.setProfilePictureUri(profilePictureUri);
        }

        public MediaSearchResult build() {
            return new MediaSearchResult(this);
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

    }

}
