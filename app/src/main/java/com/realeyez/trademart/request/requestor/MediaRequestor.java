package com.realeyez.trademart.request.requestor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;

import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.util.CacheFile;

import android.net.Uri;

public class MediaRequestor {

    public static Uri sendRequest(int mediaId, File cacheDir)
            throws JSONException, FileNotFoundException, IOException {
        String path = new StringBuilder()
            .append("/media/")
            .append(mediaId)
            .toString();
        Response response = RequestUtil.sendGetRequest(path);
        String filename = response.getContentDispositionField("filename");
        byte[] data = response.getContentBytes();

        CacheFile cache = CacheFile.cache(cacheDir, filename, data);

        return Uri.fromFile(cache.getFile());
    }
    
}
