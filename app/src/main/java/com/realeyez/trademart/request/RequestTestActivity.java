package com.realeyez.trademart.request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.realeyez.trademart.R;
import com.realeyez.trademart.request.Request.RequestBuilder;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

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
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    Request request = new RequestBuilder()
                        .useSSL()
                        .setHost("thinkpad-x230.taila38b71.ts.net")
                        .noPort()
                        .setGet()
                        .setPath("/user/59789")
                        .build();
                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder
                        .append("response data: ")
                        .append(request.sendRequest().getContent());
                    Logger.log(responseBuilder.toString(), LogLevel.CRITICAL);

                    runOnUiThread(() -> {
                        Toast.makeText(this, responseBuilder.toString(), Toast.LENGTH_LONG).show();
                        Dialogs.showMessageDialog(responseBuilder.toString(), this);
                    });
                } catch (Exception e){
                    Logger.log("something bad happened", LogLevel.CRITICAL);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("type of exception: ")
                        .append(e.getClass().toString())
                        .append(" message: ")
                        .append(e.getMessage());
                    Logger.log(errorBuilder.toString(), LogLevel.CRITICAL);
                }
            });
        });
    }

}
