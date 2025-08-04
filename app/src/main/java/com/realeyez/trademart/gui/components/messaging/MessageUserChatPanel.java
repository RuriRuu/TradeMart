package com.realeyez.trademart.gui.components.messaging;

import com.realeyez.trademart.messaging.MessageChat;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.R;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageUserChatPanel extends ConstraintLayout implements MessageChatPanel {

    private CircleImageView profilePicture;
    private TextView timestampLabel;
    private TextView messageLabel;

    private MessageChat chat;

    public MessageUserChatPanel(Context context) {
        super(context);
    }

    public MessageUserChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageUserChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MessageUserChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        profilePicture = findViewById(R.id.chat_user_profile_image);
        messageLabel = findViewById(R.id.chat_user_message_text);
        timestampLabel = findViewById(R.id.chat_user_message_timestamp);
    }

    private void loadData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        messageLabel.setText(chat.getMessage());
        timestampLabel.setText(chat.getTimeSent().format(formatter));
        profilePicture.setImageURI(chat.getProfilePictureUri());

    }

    public void setImageUri(Uri uri){
        profilePicture.setImageURI(uri);
    }


    private void setChat(MessageChat chat){
        this.chat = chat;
    }

    public static MessageUserChatPanel inflate(LayoutInflater inflater, MessageChat chat) {
        MessageUserChatPanel panel = (MessageUserChatPanel) inflater.inflate(R.layout.message_user, null, false);
        panel.setChat(chat);
        panel.loadData();
        return panel;
    }

}
