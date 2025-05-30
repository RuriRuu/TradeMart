package com.realeyez.trademart.request;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;


public class Content {

    private JSONObject content;

    public Content(ContentBuilder builder) {
        this.content = builder.content;
    }
    
    public JSONObject getContent(){
        return content;
    }

    public String getContentString(){
        return content.toString();
    }

    public static class ContentBuilder {
        private JSONObject content;

        public ContentBuilder(){
            content = new JSONObject();
        }

        public ContentBuilder put(String key, String data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentBuilder put(String key, int data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentBuilder put(String key, double data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentBuilder put(String key, boolean data){
            try {
                content.put(key, data);
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentBuilder put(String key, Content data){
            try {
                content.put(key, data.getContent());
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public ContentBuilder put(String key, ContentArray data){
            try {
                content.put(key, data.getContent());
            } catch (JSONException e) {
                StringBuilder builder = new StringBuilder()
                    .append("Could not put the entry ")
                    .append(key);
                Logger.log(builder.toString(), LogLevel.WARNING);
                e.printStackTrace();
            }
            return this;
        }

        public Content build(){
            return new Content(this);
        }

    }
    
}
