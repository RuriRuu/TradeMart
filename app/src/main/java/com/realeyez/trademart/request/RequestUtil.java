package com.realeyez.trademart.request;

import java.io.IOException;

public class RequestUtil {

    // Use this host if the app is being run under an emulator. DO NOT CHANGE THIS
    public static final String DEFAULT_HTTP_HOST = "10.0.2.2";

    /**
     * Use this host if the app is being run outside of an emulator. If there
     * is a change in the host, make sure to EDIT THIS.
     */
    public static final String DEFAULT_HTTPS_HOST = "thinkpad-x230.taila38b71.ts.net";

    RequestUtil(){
    }

    /**
     * This is a convenience method for making a GET request. Make sure to USE THIS
     * when making a request so that when there needs to be a change with the host,
     * you only need to edit this method.
     *
     * @param path the path of the requested resource
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendGetRequest(String path) throws IOException {
        Request request = new Request.RequestBuilder()
            .setGet()
            // .useSSL()
            .setHost(DEFAULT_HTTP_HOST)
            // .noPort()
            .setPath(path)
            .build();
        return request.sendRequest();
    }

    public static Request createGetRequest(String path) throws IOException {
        Request request = new Request.RequestBuilder()
            .setGet()
            // .useSSL()
            .setHost(DEFAULT_HTTP_HOST)
            // .noPort()
            .setPath(path)
            .build();
        return request;
    }

    public static Response sendPostRequest(String path, byte[] content, ContentDisposition disposition) throws IOException {
        Request request = new Request.RequestBuilder()
            .setPost(content)
            .setContentType("application/json")
            // .useSSL()
            .setHost(DEFAULT_HTTP_HOST)
            // .noPort()
            .setPath(path)
            .setContentDisposition(disposition)
            .build();
        return request.sendRequest();
    }

    public static Response sendPostRequest(String path, byte[] content) throws IOException {
        Request request = new Request.RequestBuilder()
            .setPost(content)
            .setContentType("application/json")
            // .useSSL()
            .setHost(DEFAULT_HTTP_HOST)
            // .noPort()
            .setPath(path)
            .build();
        return request.sendRequest();
    }

    /**
     * This is a convenience method for making a POST request. Make sure to USE THIS
     * when making a request so that when there needs to be a change with the host,
     * you only need to edit this method.
     *
     * @param path the path of the requested resource
     * @param content the {@link Content} to be sent to the server
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String path, Content content) throws IOException {
        Request request = new Request.RequestBuilder()
            .setPost(content.getContentString())
            .setContentType("application/json")
            // .useSSL()
            .setHost(DEFAULT_HTTP_HOST)
            // .noPort()
            .setPath(path)
            .build();
        return request.sendRequest();
    }

    /**
     * DO NOT use this method. Only use this if you need to temporarily override the
     * current {@link RequestUtil#sendGetRequest(String)} functionality
     * without editing this file.
     *
     * @param host the desired host override
     * @param port the desired port override
     * @param useSSL wether or not the override URI is http or https (true if https)
     * @param path the path of the requested resource
     *
     * @return the resulting {@link Response} of the sent request
     */
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

    /**
     * DO NOT use this method. Only use this if you need to temporarily override the
     * current {@link RequestUtil#sendPostRequest(String, Content)} functionality
     * without editing this file.
     *
     * @param host the desired host override
     * @param port the desired port override
     * @param useSSL wether or not the override URI is http or https (true if https)
     * @param path the path of the requested resource
     * @param content the {@link Content} to be sent to the server
     *
     * @return the resulting {@link Response} of the sent request
     */
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
