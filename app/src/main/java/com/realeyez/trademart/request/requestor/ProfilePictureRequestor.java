package com.realeyez.trademart.request.requestor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;

import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.CacheFile;

import android.net.Uri;

public class ProfilePictureRequestor {

    public static Uri sendRequest(int userId, File cacheDir)
            throws JSONException, FileNotFoundException, IOException {
        String path = new StringBuilder()
            .append("/user/")
            .append(userId)
            .append("/avatar")
            .toString();
        Response response = RequestUtil.sendGetRequest(path);
        String filename = response.getContentDispositionField("filename");
        byte[] profilePictureData = response.getContentBytes();

        CacheFile cache = CacheFile.cache(cacheDir, filename, profilePictureData);

        return Uri.fromFile(cache.getFile());
    }
    
}
