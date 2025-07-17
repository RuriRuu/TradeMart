package com.realeyez.trademart.messaging;

import java.time.LocalDateTime;

public class PaymentChat extends Chat {

    private int paymentId;

    protected PaymentChat(Builder builder) {
        super(builder);
        paymentId = builder.paymentId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public static class Builder extends Chat.Builder {
        
        private int paymentId;

        public Builder(){
            paymentId = -1;
        }

        public Builder setPaymentId(int paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public PaymentChat build(){
            return new PaymentChat(this);
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

        public static Builder of(Chat.Builder builder){
            Chat chat = builder.build();

            return new PaymentChat.Builder()
                .setChatId(chat.getChatId())
                .setTimeSent(chat.getTimeSent())
                .setSenderId(chat.getSenderId())
                .setConvoId(chat.getConvoId());
        }
    }

}

