package com.realeyez.trademart.request;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Response {

    private int code;
    private byte[] content;
    private String contentType;
    private long contentLength;
    private String location;
    private ContentRange contentRange;
    private ContentDisposition contentDisposition;
    
    public Response(ResponseBuilder builder){
        this.code = builder.code;
        this.content = builder.content;
        this.contentLength = builder.contentLength;
        this.contentType = builder.contentType;
        this.location = builder.location;
        this.contentRange = builder.contentRange;
        this.contentDisposition = builder.contentDisposition;
    }

    public int getCode() {
        return code;
    }

    public byte[] getContentBytes() {
        return content;
    }

    public String getContent() {
        return new String(content);
    }

    public JSONObject getContentJson() throws JSONException {
        return new JSONObject(new JSONTokener(new String(content)));
    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getLocation() {
        return location;
    }

    public ContentRange getContentRange() {
        return contentRange;
    }

    public ContentDisposition getContentDisposition() {
        return contentDisposition;
    }

    public String getContentDispositionField(String key) {
        return contentDisposition.getField(key);
    }


    public static class ResponseBuilder {

        private int code;
        private byte[] content;
        private String contentType;
        private long contentLength;
        private String location;
        private ContentRange contentRange;
        private ContentDisposition contentDisposition;

        public ResponseBuilder() {
            code = 0;
            location = contentType = null;
            content = null;
            contentRange = null;
            contentDisposition = null;
        }

        public ResponseBuilder setCode(int code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder setContent(byte[] content) {
            this.content = content;
            return this;
        }

        public ResponseBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public ResponseBuilder setContentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public ResponseBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public ResponseBuilder setContentRange(ContentRange contentRange) {
            this.contentRange = contentRange;
            return this;
        }

        public ResponseBuilder setContentDisposition(String contentDispositionString) {
            if(contentDispositionString != null && !contentDispositionString.equals("")){
                this.contentDisposition = new ContentDisposition(contentDispositionString);
            }
            return this;
        }

        public Response build(){
            return new Response(this);
        }

    }

}
