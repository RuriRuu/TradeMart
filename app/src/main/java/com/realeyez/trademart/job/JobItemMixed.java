package com.realeyez.trademart.job;

import android.net.Uri;

public class JobItemMixed extends JobItem {

    private JobTransactionType type;
    
    public JobItemMixed(Builder builder){
        super(builder);
        type = builder.type;
    }

    public JobTransactionType getType() {
        return type;
    }

    public static class Builder extends JobItem.Builder {

        private JobTransactionType type;

        public Builder(){
            type = null;
        }

        @Override
        public Builder setEmployeeId(int employeeId) {
            return (Builder) super.setEmployeeId(employeeId);
        }

        @Override
        public Builder setEmployerId(int employerId) {
            return (Builder) super.setEmployerId(employerId);
        }

        @Override
        public Builder setUsername(String username) {
            return (Builder) super.setUsername(username);
        }

        @Override
        public Builder setTitle(String title) {
            return (Builder) super.setTitle(title);
        }

        @Override
        public Builder setProfilePictureUri(Uri profilePictureUri) {
            return (Builder) super.setProfilePictureUri(profilePictureUri);
        }

        @Override
        public Builder setTransactionId(int transactionId){
            return (Builder) super.setTransactionId(transactionId);
        }

        public Builder setType(JobTransactionType type) {
            this.type = type;
            return this;
        }

        public JobItemMixed build(){
            return new JobItemMixed(this);
        }

    }

}
