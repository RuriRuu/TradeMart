package com.realeyez.trademart.profile;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.realeyez.trademart.media.MediaPicker;
import com.realeyez.trademart.request.ContentDisposition;
import com.realeyez.trademart.request.RequestUtil;

public class ProfilePictureUpdater {

    private int userId;
    private MediaPicker picker;

    public ProfilePictureUpdater(int userId, MediaPicker picker){
        this.userId = userId;
        this.picker = picker;
    }

    public void update(){
        picker.show();
    }

    public void sendUpdateRequest(MediaPicker picker){
        String path = new StringBuilder()
            .append("/user/")
            .append(userId)
            .append("/avatar/update")
            .toString();
        String filename = picker.getFilename();
        byte[] data = picker.readBytes();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                ContentDisposition disposition = ContentDisposition.attachment()
                    .addDisposition("filename", filename);
                RequestUtil.sendPostRequest(path, data, disposition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
