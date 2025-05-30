package com.realeyez.trademart.request;

import org.json.JSONArray;
import org.json.JSONException;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;


public class ContentArray {

    private JSONArray content;

    public ContentArray(ContentArrayBuilder builder) {
        this.content = builder.content;
    }
    
    public JSONArray getContent(){
        return content;
    }

    public static class ContentArrayBuilder {
        private JSONArray content;

        public ContentArrayBuilder(){
            content = new JSONArray();
        }

        public ContentArrayBuilder put(String key, String data){
            content.put(data);
            return this;
        }

        public ContentArrayBuilder put(String key, int data){
            content.put(data);
            return this;
        }

        public ContentArrayBuilder put(String key, double data){
            try {
                content.put(data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not append the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentArrayBuilder put(String key, boolean data){
            content.put(data);
            return this;
        }

        public ContentArrayBuilder put(String key, Content data){
            content.put(data.getContent());
            return this;
        }

        public ContentArrayBuilder put(String key, ContentArray data){
            content.put(data.getContent());
            return this;
        }

        public ContentArray build(){
            return new ContentArray(this);
        }

    }
    
}
