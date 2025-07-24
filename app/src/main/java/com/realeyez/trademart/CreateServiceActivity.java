package com.realeyez.trademart;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.gui.components.categorychooser.CategoryChooser;
import com.realeyez.trademart.gui.components.createpost.ImagePanel;
import com.realeyez.trademart.media.MediaPicker;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.ContentDisposition;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.FileUtil;
import com.realeyez.trademart.util.VideoThumbnailer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

public class CreateServiceActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button postButton;
    private Button addImageButton;
    private EditText titleField;
    private EditText descField;
    private EditText priceField;

    private LinearLayout image_parent_panel;
    
    private FrameLayout categoryPanel;

    private CategoryChooser categoryChooser;

    private ArrayList<ImagePanel> imagePanels;

    private final ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(!pickedImageAction(result)){
                    Dialogs.showErrorDialog("Unable to prepare the file", this);
                }
            });
    private MediaPicker picker;

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
        addImageButton.setNextFocusForwardId(R.id.createservice_post_button);
        picker = new MediaPicker(this, pickerLauncher);

        categoryPanel = findViewById(R.id.createservice_categories_input_panel);
        categoryChooser = CategoryChooser.inflate(this,
                new LayoutParams(LayoutParams.MATCH_PARENT, 
                    LayoutParams.WRAP_CONTENT));

        categoryPanel.addView(categoryChooser);

        addOnClickListeners();
    }

    private void backButtonAction(View view) {
        finish();
    }

    private void postButtonAction(View view) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Response response = sendPublishServiceRequest();
            if(response == null){
                return;
            }
            int serviceId = -1;
            try {
                JSONObject json = response.getContentJson();
                serviceId = json.getInt("service_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(serviceId == -1) return;
            for (ImagePanel panel : imagePanels) {
                Uri imageUri = panel.getImageUri();
                // TODO: display if uploading image failed
                sendPublishServiceImageRequest(imageUri, serviceId);
            }
            runOnUiThread(() -> finish());
        });
    }

    private boolean pickedImageAction(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK)
            return false;
        Intent data = result.getData();

        if (data == null || data.getData() == null)
            return false;

        Uri imageUri = data.getData();
        try {
            addImageRow(imageUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addImageRow(Uri imageUri) throws FileNotFoundException {
        ImagePanel imagePanel = null;
        if(FileUtil.getExtension(imageUri.getPath()).equals("mp4")){
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(imageUri, "r");
            Bitmap thumbnail = VideoThumbnailer.generateThumbnailBitmap(fd.getFileDescriptor());
            imagePanel = new ImagePanel(this, image_parent_panel, imageUri, thumbnail, imagePanels);
        } else {
            imagePanel = new ImagePanel(this, image_parent_panel, imageUri, imagePanels);
        }
        imagePanels.add(imagePanel);
        image_parent_panel.addView(imagePanel.getLayout());
    }

    private Response sendPublishServiceRequest(){
        if (imagePanels.size() == 0) {
            runOnUiThread(() -> {
                Dialogs.showWarningDialog("Please select media to upload", this);
            });
            return null;
        }
        String title = titleField.getText().toString();
        String description = descField.getText().toString();
        double amount = Double.parseDouble(priceField.getText().toString());

        Content content = new Content.ContentBuilder()
            .put("service_title", title)
            .put("service_description", description)
            .put("service_price", amount)
            .put("date_posted", LocalDateTime.now().toString())
            .put("owner_id", ResourceRepository.getResources().getCurrentUser().getId())
            .put("categories", categoryChooser.getChosenCategoriesString())
            .build();
        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/service/create", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Response sendPublishServiceImageRequest(Uri imageUri, int serviceId){
        byte[] bytes = readImageData(imageUri);

        assert bytes != null && bytes.length > 0 : "bytes should not be null or empty";

        String filename = getFileNameFromUri(imageUri);
        Response response = null;
        try {
            ContentDisposition disposition = ContentDisposition.attachment()
                .addDisposition("filename", filename);
            response = RequestUtil.sendPostRequest(String.format("/service/create/%d/media", serviceId), bytes, disposition);
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
            picker.show();
        });
    }

}
