package com.realeyez.trademart;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {

    Button backButton;
    Button postButton;
    Button addImageButton;

    EditText titleField;
    EditText descField;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_post);
        initComponents();
    }

    private void initComponents(){
        backButton = findViewById(R.id.createpost_back_button);
        postButton = findViewById(R.id.createpost_post_button);
        addImageButton = findViewById(R.id.createpost_add_image_button);

        titleField = findViewById(R.id.createpost_title_field);
        descField = findViewById(R.id.createpost_description_field);
        addOnClickListeners();
    }

    private void backButtonAction(View view){
        finish();
    }

    private void postButtonAction(View view){
    }

    private void addImageButtonAction(View view){
    }

    private void addOnClickListeners(){
        backButton.setOnClickListener(view -> {
            backButtonAction(view);
        });
        postButton.setOnClickListener(view -> {
            postButtonAction(view);
        });
        addImageButton.setOnClickListener(view -> {
            addImageButtonAction(view);
        });
    }

}
