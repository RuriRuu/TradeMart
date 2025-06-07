package com.realeyez.trademart;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.Encoder;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), 
            result -> {
                pickedImageAction(result);
            });
    private void addImageButtonAction(View view){
        Intent pickerIntent = new Intent(Intent.ACTION_PICK);
        pickerIntent.setType("image/*");
        launcher.launch(pickerIntent);
    }

    private void pickedImageAction(ActivityResult result){
        if (result.getResultCode() != Activity.RESULT_OK)
            return;
        Intent data = result.getData();

        if (data == null || data.getData() == null) return;

        Uri imageURI = data.getData();
        byte[] bytes = null;
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try (InputStream stream = getContentResolver().openInputStream(imageURI)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while((bytesRead = stream.read(buffer)) != -1){
                outstream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = outstream.toByteArray();

        String filename = getFileNameFromUri(imageURI);
        Logger.log("filename apparently: " + filename, LogLevel.CRITICAL);
        String fileData = Encoder.encodeBase64(bytes);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Content content = new Content.ContentBuilder()
                .put("filename", filename)
                .put("data", fileData)
                .build();
            Logger.log("log data:\n" + content.getContentString(), LogLevel.CRITICAL);
            try {
                Response response = RequestUtil.sendPostRequest("/media/upload", content);
                int status = response.getCode();
                Logger.log("status: " + status, LogLevel.CRITICAL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getFileNameFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
        }
        return "";
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
