package com.realeyez.trademart.gui.components.messaging;

import com.realeyez.trademart.messaging.PaymentChat;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PaymentReceiverChatPanel extends ConstraintLayout {

    private TextView timestampLabel;
    private TextView amountLabel;
    private TextView reasonLabel;

    private Button receivedButton;

    private OnClickListener onConfirmListener;

    private PaymentChat chat;

    public PaymentReceiverChatPanel(Context context) {
        super(context);
    }

    public PaymentReceiverChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaymentReceiverChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PaymentReceiverChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        amountLabel = findViewById(R.id.payment_receiver_amount);
        timestampLabel = findViewById(R.id.payment_receiver_message_timestamp);
        reasonLabel = findViewById(R.id.payment_receiver_reason);

        receivedButton = findViewById(R.id.payment_receiver_received);

        receivedButton.setOnClickListener(view -> {
            onConfirmListener.onClick(view);
            receivedButton.setText("Payment Confirmed");
            receivedButton.setEnabled(false);
            receivedButton.setBackgroundColor(getResources().getColor(R.color.grey, null));
        });
    }

    private void loadData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        amountLabel.setText(new StringBuilder().append(chat.getAmount()).toString());
        timestampLabel.setText(chat.getTimeSent().format(formatter));
        reasonLabel.setText(chat.getPaidFor());

        if(chat.isConfirmed()){
            receivedButton.setText("Payment Confirmed");
            receivedButton.setEnabled(false);
            receivedButton.setBackgroundColor(getResources().getColor(R.color.grey, null));
        }
    }

    private void setChat(PaymentChat chat){
        this.chat = chat;
    }

    public static PaymentReceiverChatPanel inflate(LayoutInflater inflater, PaymentChat chat) {
        PaymentReceiverChatPanel panel = (PaymentReceiverChatPanel) inflater.inflate(R.layout.payment_message_receiver, null, false);
        panel.setChat(chat);
        panel.loadData();
        return panel;
    }

    public void setOnConfirmListener(OnClickListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

}

