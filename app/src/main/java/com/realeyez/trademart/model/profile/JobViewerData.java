package com.realeyez.trademart.model.profile;

import java.util.ArrayList;

public class JobViewerData {

    private int jobId;
    private ArrayList<Integer> mediaIds;
    private String username;

    public JobViewerData(Builder builder){
        jobId = builder.jobId;
        mediaIds = builder.mediaIds;
        username = builder.username;
    }

    public String getUsername() {
        return username;
    }

    public int getJobId() {
        return jobId;
    }

    public ArrayList<Integer> getMediaIds() {
        return mediaIds;
    }

    public static class Builder {

        private int jobId;
        private ArrayList<Integer> mediaIds;
        private String username;

        public Builder(){
            jobId = 0;
            username = "";
            mediaIds = null;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setJobId(int jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder setMediaIds(ArrayList<Integer> mediaIds) {
            this.mediaIds = mediaIds;
            return this;
        }

        public JobViewerData build(){
            return new JobViewerData(this);
        }

    }
    
}
