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

public class MediaUserChatPanel extends ConstraintLayout{

    private CircleImageView profilePicture;
    private TextView timestampLabel;
    private ImageView mediaView;

    private MediaChat chat;

    public MediaUserChatPanel(Context context) {
        super(context);
    }

    public MediaUserChatPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaUserChatPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MediaUserChatPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        profilePicture = findViewById(R.id.photo_user_profile_image);
        timestampLabel = findViewById(R.id.user_photo_message_timestamp);
        mediaView = findViewById(R.id.user_photo_message_photo);
    }

    private void loadData(Uri mediaUri){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        timestampLabel.setText(chat.getTimeSent().format(formatter));
        profilePicture.post(() -> profilePicture.setImageURI(chat.getProfilePictureUri()));
        mediaView.setImageURI(mediaUri);
    }

    private void setChat(MediaChat chat){
        this.chat = chat;
    }


    public static MediaUserChatPanel inflate(LayoutInflater inflater, MediaChat chat, Uri mediaUri) {
        MediaUserChatPanel panel = (MediaUserChatPanel) inflater.inflate(R.layout.photo_message_user, null, false);
        panel.setChat(chat);
        panel.loadData(mediaUri);
        return panel;
    }


}
