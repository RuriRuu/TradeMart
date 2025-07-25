package com.realeyez.trademart.search;

import android.net.Uri;

import org.json.JSONObject;

public class UserSearchResult extends SearchResult {

    public UserSearchResult(Builder builder){
        super(builder);
    }

    public static class Builder extends SearchResult.Builder {

        public Builder(){
        }

        @Override
        public Builder setProfilePictureUri(Uri profilePictureUri) {
            return (Builder) super.setProfilePictureUri(profilePictureUri);
        }

        @Override
        public UserSearchResult.Builder setId(int id) {
            return (Builder) super.setId(id);
        }

        @Override
        public UserSearchResult.Builder setTerm(String term) {
            return (Builder) super.setTerm(term);
        }

        @Override
        public UserSearchResult.Builder setRelPoints(double relPoints) {
            return (Builder) super.setRelPoints(relPoints);
        }

        @Override
        public UserSearchResult.Builder setEntity(JSONObject entity) {
            return (Builder) super.setEntity(entity);
        }

        public UserSearchResult build() {
            return new UserSearchResult(this);
        }

    }


}
