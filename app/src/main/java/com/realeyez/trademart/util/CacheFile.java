package com.realeyez.trademart.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheFile {

    private static final String FILE_NAME_FORMAT = "tmp_%s";

    private File file;
    private String filename;

    private CacheFile(){
    }

    private static CacheFile createCacheFile(File cacheDir, String filename, byte[] data){
        CacheFile cacheFile = new CacheFile();
        String cacheFileName = String.format(FILE_NAME_FORMAT, filename);
        cacheFile.filename = filename;
        cacheFile.file = new File(cacheDir, cacheFileName);
        if(data == null)
            return cacheFile;
        if(!cacheFile.file.exists()){
            try(FileOutputStream os = new FileOutputStream(cacheFile.file)) {
                os.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return cacheFile;
    }

    public static CacheFile newFile(File cacheDir, String filename, byte[] data){
        return createCacheFile(cacheDir, filename, data);
    }
    
    public static CacheFile existing(File cacheDir, String filename){
        return createCacheFile(cacheDir, filename, null);
    }

    public static CacheFile cache(File cacheDir, String filename, byte[] data){
        CacheFile file = new CacheFile();
        file.file = new File(cacheDir, filename);
        if(!file.file.exists()){
            FileUtil.writeToFile(file.file, data);
        }
        return file;
    }

    public String getFilename() {
        return filename;
    }

    public File getFile() {
        return file;
    }
    
}
