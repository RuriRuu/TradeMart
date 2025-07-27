package com.realeyez.trademart.gui.components.messaging;

import com.realeyez.trademart.messaging.PaymentChat;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PaymentSenderChatPanel extends ConstraintLayout {

    private TextView timestampLabel;
    private TextView amountLabel;
    private TextView reasonLabel;

    private ImageView receivedIcon;
    private TextView receivedLabel;

    private PaymentChat chat;

    public PaymentSenderChatPanel(Context context) {
        super(context);
    }

    public PaymentSenderChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaymentSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PaymentSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        timestampLabel = findViewById(R.id.payment_sender_timestamp);
        amountLabel = findViewById(R.id.payment_sender_amount);
        reasonLabel = findViewById(R.id.payment_sender_reason);
        receivedIcon = findViewById(R.id.payment_sender_received_icon);
        receivedLabel = findViewById(R.id.payment_sender_received_prompt);
    }

    private void loadData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        amountLabel.setText(new StringBuilder().append(chat.getAmount()).toString());
        timestampLabel.setText(chat.getTimeSent().format(formatter));
        reasonLabel.setText(chat.getPaidFor());
        receivedLabel.setText(chat.isConfirmed() ? "Payment Confirmed!" : "Waiting for confirmation...");
        receivedIcon.setImageDrawable(getResources().getDrawable(chat.isConfirmed() ? R.drawable.circle_check : R.drawable.grayed_circle, null));
    }

    private void setChat(PaymentChat chat){
        this.chat = chat;
    }

    public static PaymentSenderChatPanel inflate(LayoutInflater inflater, PaymentChat chat) {
        PaymentSenderChatPanel panel = (PaymentSenderChatPanel) inflater.inflate(R.layout.payment_message_sender, null, false);
        panel.setChat(chat);
        panel.loadData();
        return panel;
    }

}

