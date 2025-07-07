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

    private RequestUtil(){}

    /**
     * This will be the base of all of the main utility methods here. EDIT THIS if
     * there needs to be a change in how requests are made.
     *
     * @param path the HTTP endpoint to be accessed
     *
     * @return {@link Request.RequestBuilder}
     */
    private static Request.RequestBuilder createBaseRequestBuilder(String path){
        return new Request.RequestBuilder()
            .useSSL()
            .setHost(DEFAULT_HTTPS_HOST)
            .noPort()
            .setPath(path);
    }

    /**
     * This is a convenience method for making a GET request. Make sure to USE THIS
     * when making a GET request.
     *
     * @param path the path of the requested resource
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendGetRequest(String path) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setGet()
            .build();
        return request.sendRequest();
    }

    /**
     * This is a convenience method for making a POST request. Make sure to USE THIS
     * when making a POST request that contains raw bytes content and
     * {@link ContentDisposition}
     *
     * @param path        The HTTP endpoint to be accessed
     * @param content     The byte data to be sent to the server
     * @param disposition The Content-Disposition header of the request. Usually
     *                    contains a filename.
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String path, byte[] content, ContentDisposition disposition) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content)
            .setContentDisposition(disposition)
            .build();
        return request.sendRequest();
    }

    /**
     * This is a convenience method for making a POST request. Make sure to USE THIS
     * when making a POST request that contains raw bytes content.
     *
     * @param path        The path of the requested resource
     * @param content     The byte data to be sent to the server
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String path, byte[] content) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content)
            .build();
        return request.sendRequest();
    }

    /**
     * This is a convenience method for making a POST request. Make sure to USE THIS
     * when making a POST request that contains JSON data.
     *
     * @param path    The HTTP endpoint to be accessed
     * @param content The {@link Content} to be sent to the server
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String path, Content content) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content.getContentString())
            .build();
        return request.sendRequest();
    }

    /**
     * This is a convenience method for making a POST request. Make sure to USE THIS
     * when making a POST request that contains raw bytes content and
     * {@link ContentDisposition}
     *
     * @param path        The HTTP endpoint to be accessed
     * @param content     The {@link Content} to be sent to the server
     * @param disposition The Content-Disposition header of the request. Usually
     *                    contains a filename.
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String path, Content content, ContentDisposition disposition) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content.getContentString())
            .setContentDisposition(disposition)
            .build();
        return request.sendRequest();
    }

    /**
     * Creates a GET request but doesn't send it. Useful when when trying to access
     * information about the request before sending it.
     *
     * @param path The HTTP endpoint to be accessed
     *
     * @return {@link Request}
     */
    public static Request createGetRequest(String path) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setGet()
            .build();
        return request;
    }

    /**
     * Creates a POST request but doesn't send it. Useful when when trying to access
     * information about the request before sending it.
     *
     * @param path        The HTTP endpoint to be accessed
     * @param content     The byte data to be sent to the server
     * @param disposition The Content-Disposition header of the request. Usually
     *                    contains a filename.
     *
     * @return {@link Request}
     */
    public static Request createPostRequest(String path, byte[] content, ContentDisposition disposition) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content)
            .setContentDisposition(disposition)
            .build();
        return request;
    }

    /**
     * Creates a POST request but doesn't send it. Useful when when trying to access
     * information about the request before sending it.
     *
     * @param path    The HTTP endpoint to be accessed
     * @param content The byte data to be sent to the server
     *
     * @return {@link Request}
     */
    public static Request createPostRequest(String path, byte[] content) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content)
            .build();
        return request;
    }

    /**
     * Creates a POST request but doesn't send it. Useful when when trying to access
     * information about the request before sending it.
     *
     * @param path    The HTTP endpoint to be accessed
     * @param content The {@link Content} to be sent to the server
     *
     * @return {@link Request}
     */
    public static Request createPostRequest(String path, Content content) throws IOException {
        Request request = createBaseRequestBuilder(path)
            .setPost(content.getContentString())
            .build();
        return request;
    }

    /**
     * DO NOT use this method. Only use this if you need to temporarily override the
     * current {@link RequestUtil#sendGetRequest(String)} functionality
     * without editing this file.
     *
     * @param host the desired host override
     * @param port the desired port override
     * @param useSSL whether or not the override URI is http or https (true if https)
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
     * @param disposition the {@link ContentDisposition} header of the request
     * @param content the {@link Content} to be sent to the server
     *
     * @return the resulting {@link Response} of the sent request
     */
    public static Response sendPostRequest(String host, String path, int port, boolean useSSL, ContentDisposition disposition, Content content)
            throws IOException {

        Request.RequestBuilder builder = new Request.RequestBuilder()
            .setPost(content.getContentString())
            .setHost(host)
            .setContentDisposition(disposition)
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
