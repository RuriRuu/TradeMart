package com.realeyez.trademart;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.gui.components.messaging.MessageSenderChatPanel;
import com.realeyez.trademart.gui.components.messaging.MessageUserChatPanel;
import com.realeyez.trademart.messaging.Chat;
import com.realeyez.trademart.messaging.ChatType;
import com.realeyez.trademart.messaging.MediaChat;
import com.realeyez.trademart.messaging.MessageChat;
import com.realeyez.trademart.messaging.PaymentChat;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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
import androidx.media3.common.util.Log;
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
    private int userId;

    private int receivedChats;

    private ArrayList<Chat> chats;
    private boolean setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messaging);
        initComponents();
        loadIntentExtras();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> fetchMessages());
    }

    private void initComponents() {
        receivedChats = 0;
        userId = ResourceRepository.getResources().getCurrentUser().getId();
        chats = new ArrayList<>();

        backButton = findViewById(R.id.message_back_button);
        profilePicture = findViewById(R.id.message_profile_image_view);
        convoLabel = findViewById(R.id.message_profile_name_view);

        inputField = findViewById(R.id.message_editText);

        attachButton = findViewById(R.id.message_attachButton);
        sendButton = findViewById(R.id.message_submitButton);

        scrollView = findViewById(R.id.messaging_chat_scroll);
        contentPanel = findViewById(R.id.messaging_chat_panel);

        addActionListeners();
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

    // I'm really god dam sleepy, ben on tihs for 15 minutes wjitout writing shit somehow
    private void fetchMessages(){
        Content content = new Content.ContentBuilder()
            .put("user1_id", userId)
            .put("user2_id", mateId)
            .put("received_count", receivedChats)
            .build();

        Response response;

        try {
            response = RequestUtil.sendPostRequest("/message/fetch", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String message = json.getString("message");
                runOnUiThread(() -> Dialogs.showErrorDialog(message, this));
                return;
            }
            JSONArray chatsJson = json.getJSONObject("data").getJSONArray("chats");
            for (int i = 0; i < chatsJson.length(); i++) {
                JSONObject chatJson = chatsJson.getJSONObject(i);
                Chat chat = createChatFromJson(chatJson);
                chats.add(chat);
                receivedChats++;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Dialogs.showErrorDialog("unable to load messages", this));
            return;
        }
        runOnUiThread(() -> {
            for (Chat chat : chats) {
                addChatPanel(chat);
            }
            setup = true;
        });
    }

    private void addChatPanel(Chat chat){
        if(userId == chat.getSenderId()){
            addUserChatPanel(chat);
        } else {
            addSenderChatPanel(chat);
        }
    }

    private void addUserChatPanel(Chat chat){
        switch (chat.getType()) {
            case MESSAGE:
                addMessageUserChatPanel(chat);
                break;
            case MEDIA:
                break;
            case PAYMENT:
                break;
            default:
                return;
        }
    }

    private void addSenderChatPanel(Chat chat){
        switch (chat.getType()) {
            case MESSAGE:
                addMessageSenderChatPanel(chat);
                break;
            case MEDIA:
                break;
            case PAYMENT:
                break;
            default:
                return;
        }
    }

    private void sendMessage(){
        String message = inputField.getText().toString();
        Content content = new Content.ContentBuilder()
            .put("user1_id", userId)
            .put("user2_id", mateId)
            .put("sender_id", userId)
            .put("message", message)
            .put("type", ChatType.MESSAGE.toString())
            .build();

        JSONObject chatJson;
        MessageChat chat;
        try {
            Response response = RequestUtil.sendPostRequest("/message/send", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                runOnUiThread(() -> Dialogs.showErrorDialog(rMessage, this));
                return;
            }
            chatJson = json.getJSONObject("data");
            chat = new MessageChat.Builder()
                .setChatId(chatJson.getInt("chat_id"))
                .setTimeSent(LocalDateTime.parse(chatJson.getString("time_sent")))
                .setConvoId(chatJson.getInt("convo_id"))
                .setSenderId(chatJson.getInt("sender_id"))
                .setMessage(chatJson.getString("message"))
                .build();

            inputField.setText("");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Dialogs.showErrorDialog("unable to send messages", this));
            return;
        }
        runOnUiThread(() -> addMessageUserChatPanel(chat));
    }

    private void addMessageUserChatPanel(Chat chat){
        MessageUserChatPanel panel = MessageUserChatPanel.inflate(
                getLayoutInflater(),
                (MessageChat) chat);
        addChatView(panel);
    }

    private void addMessageSenderChatPanel(Chat chat){
        MessageSenderChatPanel panel = MessageSenderChatPanel.inflate(
                getLayoutInflater(),
                (MessageChat) chat);
        addChatView(panel);
    }

    private void addChatView(View view){
        if(setup)
            contentPanel.addView(view);
        else
            contentPanel.addView(view, 0);
    }

    private Chat createChatFromJson(JSONObject json) throws JSONException {
        ChatType type = ChatType.parse(json.getString("type"));
        Chat.Builder builder = new MessageChat.Builder()
                    .setChatId(json.getInt("chat_id"))
                    .setSenderId(json.getInt("sender_id"))
                    .setConvoId(json.getInt("convo_id"))
                    // .setUsername(json.getString("username"))
                    .setTimeSent(LocalDateTime.parse(json.getString("time_sent")))
                    .setType(type);
        Chat chat = null;
        switch (type) {
            case MESSAGE:
                chat = MessageChat.Builder.of(builder)
                    .setMessage(json.getString("message"))
                    .build();
                break;
            case MEDIA:
                chat = MediaChat.Builder.of(builder)
                    .setMediaId(json.getInt("media_id"))
                    .build();
                break;
            case PAYMENT:
                chat = PaymentChat.Builder.of(builder)
                    .setPaymentId(json.getInt("payment_id"))
                    .build();
                break;
            default:
                return null;
        }

        return chat;
    }

    private void sendMessageAction(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> sendMessage());
    }

    private void addActionListeners() {
        backButton.setOnClickListener(view -> finish());
        sendButton.setOnClickListener(view -> {
            sendMessageAction();
        });
        inputField.setOnEditorActionListener((view, action, event) -> {
            if (action == EditorInfo.IME_ACTION_SEND || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN)
                sendMessageAction();
            return true;
        });
    }

}
