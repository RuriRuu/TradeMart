package com.realeyez.trademart.messaging;

import java.time.LocalDateTime;

import com.realeyez.trademart.payment.PaymentType;

public class PaymentChat extends Chat {

    private int paymentId;
    private double amount;
    private String paidFor;
    private PaymentType paymentType;

    protected PaymentChat(Builder builder) {
        super(builder);
        paymentId = builder.paymentId;
        amount = builder.amount;
        paidFor = builder.paidFor;
        paymentType = builder.paymentType;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaidFor() {
        return paidFor;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public static class Builder extends Chat.Builder {
        
        private int paymentId;
        private double amount;
        private String paidFor;
        private PaymentType paymentType;

        public Builder(){
            paymentId = -1;
            amount = 0;
            paidFor = "";
            paymentType = null;
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

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setPaidFor(String paidFor) {
            this.paidFor = paidFor;
            return this;
        }

        public Builder setPaymentType(PaymentType paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public static Builder of(Chat.Builder builder){
            Chat chat = builder.build();

            return new PaymentChat.Builder()
                .setChatId(chat.getChatId())
                .setTimeSent(chat.getTimeSent())
                .setType(chat.getType())
                .setSenderId(chat.getSenderId())
                .setConvoId(chat.getConvoId());
        }
    }

}

