package com.realeyez.trademart.messaging;

import java.time.LocalDateTime;

import android.net.Uri;

public class MessageChat extends Chat {

    private String message;

    protected MessageChat(Builder builder) {
        super(builder);
        message = builder.message;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder extends Chat.Builder {

        private String message;

        public Builder(){
            message = "";
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public MessageChat build(){
            return new MessageChat(this);
        }

        @Override
        public Builder setChatId(int chatId) {
            return (Builder) super.setChatId(chatId);
        }

        @Override
        public Builder setConvoId(int convoId) {
            return (Builder) super.setConvoId(convoId);
        }

        @Override
        public Builder setSenderId(int senderId) {
            return (Builder) super.setSenderId(senderId);
        }

        @Override
        public Builder setTimeSent(LocalDateTime timeSent) {
            return (Builder) super.setTimeSent(timeSent);
        }

        @Override
        public Builder setType(ChatType type) {
            return (Builder) super.setType(type);
        }

        @Override
        public Builder setProfilePictureUri(Uri profilePictureUri) {
            return (Builder) super.setProfilePictureUri(profilePictureUri);
        }

        @Override
        public Builder setUsername(String username) {
            return (Builder) super.setUsername(username);
        }

        public static Builder of(Chat.Builder builder){
            Chat chat = builder.build();

            return new MessageChat.Builder()
                .setChatId(chat.getChatId())
                .setTimeSent(chat.getTimeSent())
                .setProfilePictureUri(chat.getProfilePictureUri())
                .setType(chat.getType())
                .setSenderId(chat.getSenderId())
                .setConvoId(chat.getConvoId());
        }

    }
    
}

