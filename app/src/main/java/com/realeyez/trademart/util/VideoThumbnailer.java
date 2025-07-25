package com.realeyez.trademart.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;

public class VideoThumbnailer {

    public static File generateThumbnailFile(File cacheDir, FileDescriptor fd, String filename){
        Bitmap thumbnail = null;
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()){
            retriever.setDataSource(fd);
            thumbnail = retriever.getFrameAtTime(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(CompressFormat.PNG, 0, stream);
        byte[] bytes = stream.toByteArray();
        CacheFile cacheFile = CacheFile.cache(cacheDir, FileUtil.removeExtension(filename).concat(".png"), bytes);
        return cacheFile.getFile();
    }
    
    public static Bitmap generateThumbnailBitmap(FileDescriptor fd){
        Bitmap thumbnail = null;
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()){
            retriever.setDataSource(fd);
            thumbnail = retriever.getFrameAtTime(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbnail;
    }
    
}
