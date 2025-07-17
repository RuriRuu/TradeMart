package com.realeyez.trademart.gui.components.messaging;

import java.time.format.DateTimeFormatter;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.messaging.ConvoInfo;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConvoPanel extends ConstraintLayout {

    private CircleImageView profilePicture;
    private TextView usernameLabel;
    private TextView timestampLabel;
    private TextView lastMessageLabel;

    private ConvoInfo convoInfo;

    public ConvoPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConvoPanel(Context context) {
        super(context);
    }

    public ConvoPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ConvoPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initComponents(){
        profilePicture = findViewById(R.id.convo_layout_profile_image);
        usernameLabel = findViewById(R.id.convo_username);
        timestampLabel = findViewById(R.id.convo_timestamp);
        lastMessageLabel = findViewById(R.id.convo_last_message);

        setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MessagingActivity.class);
            intent.putExtra("username", convoInfo.getUsername());
            intent.putExtra("profile_picture_uri", convoInfo.getProfilePictureUri());
            intent.putExtra("user_id", convoInfo.getUserId());
            intent.putExtra("convo_id", convoInfo.getConvoId());

            getContext().startActivity(intent);
        });
    }

    public void loadInfo(){
        usernameLabel.setText(convoInfo.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                
        timestampLabel.setText(convoInfo.getTimestamp().format(formatter));
        String lastMessage = generateLastMessage();
        lastMessageLabel.setText(lastMessage);
        profilePicture.setImageURI(convoInfo.getProfilePictureUri());
    }

    private String generateLastMessage(){
        switch(convoInfo.getType()){
            case MESSAGE:
                return convoInfo.getLastMessage();
            case MEDIA:
                return "Sent a payment!";
            case PAYMENT:
                return "Sent a media file!";
            default:
                return "Sent a message!";
        }
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    public void setConvoInfo(ConvoInfo convoInfo) {
        this.convoInfo = convoInfo;
    }

    public static ConvoPanel inflate(LayoutInflater inflater, ConvoInfo convoInfo) {
        ConvoPanel panel = (ConvoPanel) inflater.inflate(R.layout.convo_layout, null, false);
        panel.setConvoInfo(convoInfo);
        panel.loadInfo();
        return panel;
    }

}
