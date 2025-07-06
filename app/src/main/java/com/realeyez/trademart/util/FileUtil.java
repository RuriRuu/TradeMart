package com.realeyez.trademart.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.realeyez.trademart.util.Logger.LogLevel;

public class FileUtil {

    public static File writeToFile(String filename, byte[] data){
        File file = new File(filename);
        try (FileOutputStream os = new FileOutputStream(file)) {
            if(!file.exists()){
                file.createNewFile();
            }
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("Unable to write the file: ".concat(filename).concat(" (FileUtil#writeToFile(String, byte[]))"),
                    LogLevel.WARNING);
            return null;
        }
        return file;
    }

    public static File writeToFile(File file, byte[] data){
        try (FileOutputStream os = new FileOutputStream(file)) {
            if(!file.exists()){
                file.createNewFile();
            }
            os.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log(
                    "Unable to write the file: ".concat(file.getName())
                            .concat(" (FileUtil#writeToFile(File, byte[]))"),
                    LogLevel.WARNING);
            return null;
        }
        return file;
    }

    public static String getExtension(String filename){
        StringBuilder builder = new StringBuilder();
        for (int i = filename.length()-1; i >= 0; i--) {
            char curChar = filename.charAt(i);
            if(curChar != '.'){
                builder.insert(0, curChar);
                continue;
            }
            break;
        }
        return builder.toString();
    }

    public static String removeExtension(String filename){
        int extension_index = -1;
        for (int i = filename.length()-1; i >= 0; i--) {
            char curChar = filename.charAt(i);
            if(curChar == '.'){
                extension_index = i;
                break;
            }
        }
        if(extension_index == -1){
            return filename;
        }
        return filename.substring(0, extension_index);
    }

}
