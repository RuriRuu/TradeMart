package com.realeyez.trademart.messaging;

import java.time.LocalDateTime;

import android.net.Uri;

public class Chat {

    private int chatId;
    private String username;
    private ChatType type;
    private LocalDateTime timeSent;
    private int senderId;
    private int convoId;
    private Uri profilePictureUri;

    protected Chat(Builder builder){
        chatId = builder.chatId;
        type = builder.type;
        timeSent = builder.timeSent;
        senderId = builder.senderId;
        convoId = builder.convoId;
        profilePictureUri = builder.profilePictureUri;
        username = builder.username;
    }

    public int getChatId() {
        return chatId;
    }

    public ChatType getType() {
        return type;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getConvoId() {
        return convoId;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }

    public String getUsername() {
        return username;
    }

    public static class Builder {

        private int chatId;
        private ChatType type;
        private LocalDateTime timeSent;
        private int senderId;
        private int convoId;
        private Uri profilePictureUri;
        private String username;

        public Builder(){
            chatId = senderId = convoId = -1;
            timeSent = null;
            type = ChatType.MESSAGE;
            profilePictureUri = null;
            username = null;
        }

        public Builder setChatId(int chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setType(ChatType type) {
            this.type = type;
            return this;
        }

        public Builder setTimeSent(LocalDateTime timeSent) {
            this.timeSent = timeSent;
            return this;
        }

        public Builder setSenderId(int senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setConvoId(int convoId) {
            this.convoId = convoId;
            return this;
        }

        public Builder setProfilePictureUri(Uri profilePictureUri) {
            this.profilePictureUri = profilePictureUri;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        protected Chat build(){
            return new Chat(this);
        }

    }
    
}

