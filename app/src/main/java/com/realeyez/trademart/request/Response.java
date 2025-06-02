package com.realeyez.trademart.request;

public class Response {

    private int code;
    private String content;
    private String contentType;
    private String location;
    
    public Response(ResponseBuilder builder){
        this.code = builder.code;
        this.content = builder.content;
        this.contentType = builder.contentType;
        this.location = builder.location;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }

    public static class ResponseBuilder {

        private int code;
        private String content;
        private String contentType;
        private String location;

        public ResponseBuilder() {
            code = 0;
            location = contentType = content = null;
        }

        public ResponseBuilder setCode(int code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public ResponseBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public ResponseBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Response build(){
            return new Response(this);
        }

    }

}
