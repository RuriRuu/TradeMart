package com.realeyez.trademart.gui.components.messaging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import com.realeyez.trademart.R;
import com.realeyez.trademart.messaging.ConvoInfo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    }

    public void loadInfo(){
        usernameLabel.setText(convoInfo.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
                
        timestampLabel.setText(convoInfo.getTimestamp().format(formatter));
        lastMessageLabel.setText(convoInfo.getLastMessage());
        profilePicture.setImageURI(convoInfo.getProfilePictureUri());
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
