package com.realeyez.trademart.messaging;

import java.time.LocalDateTime;

import android.net.Uri;

public class ConvoInfo {

    private int convoId;
    private ChatType type;
    private int userId;
    private int senderId;
    private String username;
    private String lastMessage;
    private LocalDateTime timestamp;
    private Uri profilePictureUri;

    public ConvoInfo(Builder builder){
        convoId = builder.convoId;
        type = builder.type;
        userId = builder.userId;
        senderId = builder.senderId;
        username = builder.username;
        lastMessage = builder.lastMessage;
        timestamp = builder.timestamp;
        profilePictureUri = builder.profilePictureUri;
    }

    public int getConvoId() {
        return convoId;
    }

    public ChatType getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }

    public int getSenderId() {
        return senderId;
    }

    public static class Builder {

        private int convoId;
        private ChatType type;
        private int userId;
        private int senderId;
        private String username;
        private String lastMessage;
        private LocalDateTime timestamp;
        private Uri profilePictureUri;

        public Builder(){
            senderId = userId = convoId = -1;
            type = null;
            lastMessage = username = "";
            timestamp = null;
            profilePictureUri = null;
        }

        public Builder setConvoId(int convoId) {
            this.convoId = convoId;
            return this;
        }

        public Builder setType(ChatType type) {
            this.type = type;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setSenderId(int senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
            return this;
        }

        public Builder setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setProfilePictureUri(Uri profilePictureUri) {
            this.profilePictureUri = profilePictureUri;
            return this;
        }

        public ConvoInfo build(){
            return new ConvoInfo(this);
        }

    }
    
}
