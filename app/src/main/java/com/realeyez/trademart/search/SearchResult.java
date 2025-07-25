package com.realeyez.trademart.search;

import org.json.JSONObject;

import android.net.Uri;

public class SearchResult {

    private String term;
    private int id;
    private double relPoints;
    private JSONObject entity;
    private Uri profilePictureUri;

    public SearchResult(Builder builder){
        term = builder.term;
        id = builder.id;
        relPoints = builder.relPoints;
        entity = builder.entity;
        profilePictureUri = builder.profilePictureUri;
    }

    public String getTerm() {
        return term;
    }

    public int getId() {
        return id;
    }

    public double getRelPoints() {
        return relPoints;
    }

    public JSONObject getEntity() {
        return entity;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }

    public static class Builder {

        private String term;
        private int id;
        private double relPoints;
        private JSONObject entity;
        private Uri profilePictureUri;

        public Builder() {
            term = "";
            id = -1;
            relPoints = 0;
            entity = null;
            profilePictureUri = null;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTerm(String term) {
            this.term = term;
            return this;
        }

        public Builder setRelPoints(double relPoints) {
            this.relPoints = relPoints;
            return this;
        }

        public Builder setEntity(JSONObject entity) {
            this.entity = entity;
            return this;
        }

        public Builder setProfilePictureUri(Uri profilePictureUri) {
            this.profilePictureUri = profilePictureUri;
            return this;
        }

        public SearchResult build(){
            return new SearchResult(this);
        }
    }
}
