package com.realeyez.trademart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {

    private ImageButton backButton;
    private CircleImageView profilePicture;
    private TextView convoLabel;

    private EditText inputField;
    
    private ImageButton attachButton;
    private ImageButton sendButton;

    private ScrollView scrollView;
    private LinearLayout contentPanel;

    private int mateId;
    private int convoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messaging);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
        loadIntentExtras();
    }

    private void initComponents() {
        backButton = findViewById(R.id.message_back_button);
        profilePicture = findViewById(R.id.message_profile_image_view);
        convoLabel = findViewById(R.id.message_profile_name_view);

        inputField = findViewById(R.id.message_editText);

        attachButton = findViewById(R.id.message_attachButton);
        sendButton = findViewById(R.id.message_submitButton);

        scrollView = findViewById(R.id.messaging_chat_scroll);
        contentPanel = findViewById(R.id.messaging_chat_panel);
    }

    private void loadIntentExtras(){
        Intent intent = getIntent();
        int user_id = intent.getIntExtra("user_id", -1);
        int convo_id = intent.getIntExtra("convo_id", -1);
        Uri profilePictureUri = intent.getParcelableExtra("profile_picture_uri");
        String username = intent.getStringExtra("username");

        mateId = user_id;
        convoId = convo_id;
        convoLabel.setText(username);
        profilePicture.setImageURI(profilePictureUri);
    }

    private void fetchMessages(){
    }

}
