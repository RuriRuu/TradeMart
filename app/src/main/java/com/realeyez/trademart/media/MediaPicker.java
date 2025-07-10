package com.realeyez.trademart.media;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MediaPicker {

    private AppCompatActivity activity;
    private Uri pickedUri;


    private ActivityResultLauncher<Intent> pickerLauncher;

    public MediaPicker(AppCompatActivity activity, ActivityResultLauncher<Intent> launcher){
        this.activity = activity;
        pickerLauncher = launcher;
    }

    public void pickedImageAction(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK)
            return;
        Intent data = result.getData();

        if (data == null || data.getData() == null)
            return;

        pickedUri = data.getData();
    }

    public void show() {
        Intent pickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pickerIntent.setType("*/*");
        String[] mimetypes = {"image/*", "video/*"};
        pickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        pickerLauncher.launch(pickerIntent);
    }

    public byte[] readBytes(){
        if(pickedUri == null){
            return null;
        }
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try (InputStream stream = activity.getContentResolver().openInputStream(pickedUri)) {
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

    public String getFilename() {
        if(pickedUri == null){
            return null;
        }
        Cursor cursor = activity.getContentResolver().query(pickedUri, null, null, null, null);
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

    public Uri getPickedUri(){
        return pickedUri;
    }
    
}
