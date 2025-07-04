package com.realeyez.trademart.post;

import java.util.ArrayList;

public class PostData {

    private ArrayList<Integer> mediaIds;
    private String username;
    private int postId;
    private String title;
    private String description;

    private PostData(Builder builder){
        this.postId = builder.postId;
        this.title = builder.title;
        this.description = builder.description;
        this.mediaIds = builder.mediaIds;
        this.username = builder.username;
    }

    public String getTitle() {
        return title;
    }

    public int getPostId() {
        return postId;
    }

    public int getMediaId(int index) {
        return mediaIds.get(index);
    }

    public ArrayList<Integer> getMediaIds() {
        return mediaIds;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public static class Builder {

        private ArrayList<Integer> mediaIds;
        private int postId;
        private String username;
        private String title;
        private String description;

        public Builder(){
            postId = 0;
            username = title = description = "";
            mediaIds = null;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPostId(int postId) {
            this.postId = postId;
            return this;
        }

        public Builder setMediaIds(ArrayList<Integer> mediaIds) {
            this.mediaIds = mediaIds;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public PostData build(){
            return new PostData(this);
        }

    }
    
}
