package com.realeyez.trademart.gui.components.messaging;

import com.realeyez.trademart.messaging.MediaChat;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.R;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class MediaSenderChatPanel extends ConstraintLayout{

    private CircleImageView profilePicture;
    private TextView usernameLabel;
    private TextView timestampLabel;
    private ImageView mediaView;

    private MediaChat chat;

    public MediaSenderChatPanel(Context context) {
        super(context);
    }

    public MediaSenderChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MediaSenderChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        usernameLabel = findViewById(R.id.sender_photo_message_username);
        profilePicture = findViewById(R.id.photo_sender_profile_image);
        timestampLabel = findViewById(R.id.sender_photo_message_timestamp);
        mediaView = findViewById(R.id.sender_photo_message_photo);
    }

    private void loadData(Uri mediaUri){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        usernameLabel.setText(chat.getUsername());
        timestampLabel.setText(chat.getTimeSent().format(formatter));
        profilePicture.post(() -> profilePicture.setImageURI(chat.getProfilePictureUri()));
        mediaView.setImageURI(mediaUri);
    }

    private void setChat(MediaChat chat){
        this.chat = chat;
    }


    public static MediaSenderChatPanel inflate(LayoutInflater inflater, MediaChat chat, Uri mediaUri) {
        MediaSenderChatPanel panel = (MediaSenderChatPanel) inflater.inflate(R.layout.photo_message_sender, null, false);
        panel.setChat(chat);
        panel.loadData(mediaUri);
        return panel;
    }


}
