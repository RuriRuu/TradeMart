package com.realeyez.trademart.util;

import android.util.Base64;

public class Encoder {

    public static String encodeBase64(String data){
        return Base64.encodeToString(data.getBytes(), Base64.DEFAULT);
    }
    
    public static String encodeBase64(byte[] data){
        return Base64.encodeToString(data, Base64.DEFAULT);
    }
    
    public static byte[] decodeBase64(String data){
        return Base64.decode(data.getBytes(), Base64.DEFAULT);
    }

    public static String decodeBase64String(String data){
        return new String(Base64.decode(data, Base64.DEFAULT));
    }

}
