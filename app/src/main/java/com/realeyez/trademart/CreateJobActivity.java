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

import com.realeyez.trademart.gui.components.categorychooser.CategoryChooser;
import com.realeyez.trademart.gui.components.createpost.ImagePanel;
import com.realeyez.trademart.gui.dialogs.LoadingDialog;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.ContentDisposition;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.FileUtil;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.VideoThumbnailer;
import com.realeyez.trademart.util.Logger.LogLevel;

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

    public class CreateJobActivity extends AppCompatActivity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job_list);
        initComponents();
    }

    private void initComponents() {
        backButton = findViewById(R.id.createjob_list_back_button);
        postButton = findViewById(R.id.createjob_list_post_button);
        addImageButton = findViewById(R.id.createjob_list_add_image_button);
        priceField = findViewById(R.id.createjob_price);

        titleField = findViewById(R.id.createjob_list_title_field);
        descField = findViewById(R.id.createjob_list_description_field);
        image_parent_panel = findViewById(R.id.createjob_list_images_panel);
        imagePanels = new ArrayList<>();

        categoryPanel = findViewById(R.id.createjob_categories_input_panel);

        categoryChooser = CategoryChooser.inflate(this,
                new LayoutParams(LayoutParams.MATCH_PARENT, 
                    LayoutParams.WRAP_CONTENT));

        categoryPanel.addView(categoryChooser);

        addOnClickListeners();
    }

    private void backButtonAction(View view) {
        finish();
    }

    private void postButtonAction() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            Response response = sendPublishJobListRequest();
            if(response == null){
                return;
            }
            int jobId = -1;
            try {
                JSONObject json = response.getContentJson();
                jobId = json.getJSONObject("data").getInt("job_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(jobId == -1) return;
            for (ImagePanel panel : imagePanels) {
                Uri imageUri = panel.getImageUri();
                // TODO: display if uploading image failed
                sendPublishJobMediaRequest(imageUri, jobId);
            }
        });
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                pickedImageAction(result);
            });

    private void addImageButtonAction() {
        Intent pickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickerIntent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*"};
        pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        launcher.launch(pickerIntent);
    }

    private void pickedImageAction(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK)
            return;
        Intent data = result.getData();

        if (data == null || data.getData() == null)
            return;

        Uri imageUri = data.getData();
        try {
            addImageRow(imageUri);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
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

    private Response sendPublishJobListRequest(){
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
                .put("job_title", title)
                .put("job_description", description)
                .put("employer_id", ResourceRepository.getResources().getCurrentUser().getId())
                .put("amount", amount)
                .put("categories", categoryChooser.getChosenCategoriesString())
                .build();
        Response response = null;
        Logger.log(content.getContentString(), LogLevel.INFO);
        try {
            response = RequestUtil.sendPostRequest("/jobs/create", content);
        } catch (FileNotFoundException e) {
            runOnUiThread(() -> {
                Dialogs.showErrorDialog("Unable to publish Job List", this);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Response sendPublishJobMediaRequest(Uri imageUri, int jobId){
        byte[] bytes = readImageData(imageUri);

        String filename = getFileNameFromUri(imageUri);
        Response response = null;
        try {
            ContentDisposition disposition = ContentDisposition.attachment()
                    .addDisposition("filename", filename);
            Logger.logi(filename);
            Logger.logi("sending upload request");
            response = RequestUtil.sendPostRequest(String.format("/jobs/create/%d/media", jobId), bytes, disposition);
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
            if(imagePanels.size() == 0){
                Dialogs.showMessageDialog("Posts must have media attached", this);
                return;
            }
            LoadingDialog dialog = new LoadingDialog(this);
            dialog.show();
            postButtonAction();
            dialog.close();
            finish();
        });
        addImageButton.setOnClickListener(view -> {
            addImageButtonAction();
        });
    }

}
