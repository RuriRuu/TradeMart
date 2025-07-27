package com.realeyez.trademart.profile;

import java.io.FileNotFoundException;
import java.io.IOException;

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
        picker.show("image/*");
    }

    public void sendUpdateRequest(MediaPicker picker) throws FileNotFoundException, IOException{
        String path = new StringBuilder()
            .append("/user/")
            .append(userId)
            .append("/avatar/update")
            .toString();
        String filename = picker.getFilename();
        byte[] data = picker.readBytes();

        ContentDisposition disposition = ContentDisposition.attachment()
            .addDisposition("filename", filename);
        RequestUtil.sendPostRequest(path, data, disposition);
    }

}
