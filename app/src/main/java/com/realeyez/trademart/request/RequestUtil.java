package com.realeyez.trademart.request;

import java.io.IOException;

public class RequestUtil {

    public static final String DEFAULT_HTTP_HOST = "10.0.2.2";
    public static final String DEFAULT_HTTPS_HOST = "thinkpad-x230.taila38b71.ts.net";

    RequestUtil(){
    }

    public static Response sendGetRequest(String path) throws IOException {
        Request request = new Request.RequestBuilder()
            .setGet()
            .useSSL()
            .setHost(DEFAULT_HTTPS_HOST)
            .noPort()
            .setPath(path)
            .build();
        return request.sendRequest();
    }

    public static Response sendPostRequest(String path, Content content) throws IOException {
        Request request = new Request.RequestBuilder()
            .setPost(content.getContentString())
            .useSSL()
            .setHost(DEFAULT_HTTPS_HOST)
            .noPort()
            .setPath(path)
            .build();
        return request.sendRequest();
    }

    public static Response sendGetRequest(String host, String path, int port, boolean useSSL) throws IOException {
        Request.RequestBuilder builder = new Request.RequestBuilder()
            .setGet()
            .setHost(host)
            .setPath(path);
        if(useSSL){
            builder.useSSL();
        }
        if(port == -1){
            builder.noPort();
        } else {
            builder.setPort(port);
        }
        return builder.build().sendRequest();
    }

    public static Response sendPostRequest(String host, String path, int port, boolean useSSL, Content content)
            throws IOException {

        Request.RequestBuilder builder = new Request.RequestBuilder()
            .setPost(content.getContentString())
            .setHost(host)
            .setPath(path);
        if(useSSL){
            builder.useSSL();
        }
        if(port == -1){
            builder.noPort();
        } else {
            builder.setPort(port);
        }
        return builder.build().sendRequest();
    }

}
