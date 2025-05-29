package com.realeyez.trademart.request;

import com.realeyez.trademart.R;
import com.realeyez.trademart.request.Request.RequestBuilder;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RequestTestActivity extends AppCompatActivity {

    private Button sendButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request_test);
        initComponents();
    }

    public void initComponents(){
        sendButton = findViewById(R.id.send_req_button);
        sendButton.setOnClickListener(view -> {
            try {
                Request request = new RequestBuilder()
                    .setHost("10.0.2.2")
                    .setPost("cock and balls")
                    .setPath("/user/29123")
                    .build();
                String response = request.sendRequest();
                Logger.log(response, LogLevel.CRITICAL);
                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                Dialogs.showMessageDialog(response, this);
            } catch (Exception e){
                Logger.log("something bad happened", LogLevel.CRITICAL);
                Logger.log(e.getMessage(), LogLevel.CRITICAL);
            }
        });
    }

}
