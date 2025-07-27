package com.realeyez.trademart.job;

import android.net.Uri;

public class JobItem {

    private int transactionId;
    private int employeeId;
    private int employerId;
    private String username;
    private String title;
    private Uri profilePictureUri;
    
    public JobItem(Builder builder){
        transactionId = builder.transactionId;
        employeeId = builder.employeeId;
        employerId = builder.employerId;
        username = builder.username;
        title = builder.title;
        profilePictureUri = builder.profilePictureUri;
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

    public int getEmployeeId() {
        return employeeId;
    }

    public int getEmployerId() {
        return employerId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public static class Builder {

        private int transactionId;
        private int employeeId;
        private int employerId;
        private String username;
        private String title;
        private Uri profilePictureUri;

        public Builder(){
            transactionId = employerId = employeeId = -1;
            username = title = "";
            profilePictureUri = null;
        }

        public Builder setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder setEmployerId(int employerId) {
            this.employerId = employerId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setTransactionId(int transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setProfilePictureUri(Uri profilePictureUri) {
            this.profilePictureUri = profilePictureUri;
            return this;
        }

        public JobItem build(){
            return new JobItem(this);
        }

    }

}
