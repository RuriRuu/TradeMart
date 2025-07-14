package com.realeyez.trademart;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.gui.components.createpost.ImagePanel;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Encoder;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class CreateServiceActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button postButton;
    private Button addImageButton;
    private EditText titleField;
    private EditText descField;
    private EditText priceField;

    private LinearLayout image_parent_panel;

    private ArrayList<ImagePanel> imagePanels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
        initComponents();
    }

    private void initComponents() {
        backButton = findViewById(R.id.createservice_back_button);
        postButton = findViewById(R.id.createservice_post_button);
        addImageButton = findViewById(R.id.createservice_add_image_button);

        titleField = findViewById(R.id.createservice_title_field);
        descField = findViewById(R.id.createservice_description_field);
        priceField = findViewById(R.id.createservice_price);
        image_parent_panel = findViewById(R.id.createservice_images_panel);
        imagePanels = new ArrayList<>();
        addOnClickListeners();
    }

    private void backButtonAction(View view) {
        finish();
    }

    private void postButtonAction(View view) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Response response = sendPublishPostRequest();
            int postId = -1;
            try {
                JSONObject json = response.getContentJson();
                postId = json.getInt("post_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(postId == -1) return;
            for (ImagePanel panel : imagePanels) {
                Uri imageUri = panel.getImageUri();
                // TODO: display if uploading image failed
                sendPublishPostImageRequest(imageUri, postId);
            }
            runOnUiThread(() -> finish());
        });
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                pickedImageAction(result);
            });

    private void addImageButtonAction(View view) {
        Intent pickerIntent = new Intent(Intent.ACTION_PICK);
        pickerIntent.setType("image/*");
        launcher.launch(pickerIntent);
    }

    private void pickedImageAction(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK)
            return;
        Intent data = result.getData();

        if (data == null || data.getData() == null)
            return;

        Uri imageUri = data.getData();
        addImageRow(imageUri);
    }

    private void addImageRow(Uri imageUri){
        ImagePanel imagePanel = new ImagePanel(this, image_parent_panel, imageUri, imagePanels);
        imagePanels.add(imagePanel);
        image_parent_panel.addView(imagePanel.getLayout());
    }

    private Response sendPublishPostRequest(){
        String title = titleField.getText().toString();
        String description = descField.getText().toString();
        String price = priceField.getText().toString();

        Content content = new Content.ContentBuilder()
            .put("job_title", title)
                .put("job_description", description)
                .put("job_price", price)
            .put("job_user_id", ResourceRepository.getResources().getCurrentUser().getId())
            .build();
        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/post/publish", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Response sendPublishPostImageRequest(Uri imageUri, int postId){
        byte[] bytes = readImageData(imageUri);

        String filename = getFileNameFromUri(imageUri);
        String fileData = Encoder.encodeBase64(bytes);
        Content content = new Content.ContentBuilder()
            .put("filename", filename)
            .put("data", fileData)
            .build();
        Response response = null;
        try {
            response = RequestUtil.sendPostRequest(String.format("/post/publish/%d/media", postId), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private byte[] readImageData(Uri uri){
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try (InputStream stream = getContentResolver().openInputStream(uri)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                outstream.write(buffer, 0, bytesRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outstream.toByteArray();
    }

    private String getFileNameFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0) {
                String name = cursor.getString(nameIndex);
                cursor.close();
                return name;
            }
        }
        return "";
    }

    private void addOnClickListeners() {
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
