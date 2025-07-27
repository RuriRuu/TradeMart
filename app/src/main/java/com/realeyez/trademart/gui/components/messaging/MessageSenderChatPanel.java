package com.realeyez.trademart.gui.components.messaging;

import com.realeyez.trademart.messaging.MessageChat;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageSenderChatPanel extends ConstraintLayout{

    private CircleImageView profilePicture;
    private TextView usernameLabel;
    private TextView timestampLabel;
    private TextView messageLabel;

    private MessageChat chat;

    public MessageSenderChatPanel(Context context) {
        super(context);
    }

    public MessageSenderChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MessageSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        usernameLabel = findViewById(R.id.chat_sender_message_username);
        profilePicture = findViewById(R.id.chat_sender_profile_image);
        messageLabel = findViewById(R.id.chat_sender_message_text);
        timestampLabel = findViewById(R.id.chat_sender_message_timestamp);
    }

    private void loadData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        if(chat.getProfilePictureUri() == null)
            Logger.log("profile picture is null", LogLevel.INFO);
        else
            Logger.log(chat.getProfilePictureUri().toString(), LogLevel.INFO);
        usernameLabel.setText(chat.getUsername());
        messageLabel.setText(chat.getMessage());
        timestampLabel.setText(chat.getTimeSent().format(formatter));
        profilePicture.post(() -> profilePicture.setImageURI(chat.getProfilePictureUri()));
    }

    private void setChat(MessageChat chat){
        this.chat = chat;
    }


    public static MessageSenderChatPanel inflate(LayoutInflater inflater, MessageChat chat) {
        MessageSenderChatPanel panel = (MessageSenderChatPanel) inflater.inflate(R.layout.message_sender, null, false);
        panel.setChat(chat);
        panel.loadData();
        return panel;
    }


}
