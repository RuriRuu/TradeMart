package com.realeyez.trademart.request;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

public class Request {
    
    public static final String SERVER_HOST = "10.0.2.2";
    public static final int SERVER_PORT = 8080;

    private String host;
    private String method;
    private String path;
    private String body;
    private String contentType;
    private HttpURLConnection con;

    public Request(RequestBuilder builder){
        this.host = builder.host;
        this.method = builder.method;
        this.path = builder.path;
        this.body = builder.body;
        this.contentType = builder.contentType;
    }

    /**
     * sends the built request to the server, then returns a string containing the
     * response body sent by the server.
     *
     *
     * @return response from the server
     *
     **/
    public String sendRequest(){
        try {
            URL url = new URL(buildURL());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", contentType);
            if(method.equals("POST")){
                con.setDoOutput(true);
            }
            con.setDoInput(true);
            con.connect();

        } catch (MalformedURLException e) {
            Logger.log("MalformedURLException", LogLevel.CRITICAL);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.log("IOException when making request", LogLevel.CRITICAL);
            e.printStackTrace();
        }
        if(method.equals("POST")){
            sendContentBody();
        }
        String output = readResponseBody();
        con.disconnect();
        return output;
    }

    private void sendContentBody(){
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()))){
            writer.write(body);
        } catch(IOException e){
            Logger.log("IOException when writing request", LogLevel.CRITICAL);
            e.printStackTrace();
        }
    }

    private String readResponseBody(){
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            Logger.log("IOException when reading response", LogLevel.CRITICAL);
            e.printStackTrace();
        }
        return builder.toString();
    }
    

    private String buildURL(){
        StringBuilder builder = new StringBuilder()
            .append("http://")
            .append(host == null ? SERVER_HOST : host)
            .append(":")
            .append(SERVER_PORT)
            .append(path);
        return builder.toString();
    }

    public static class RequestBuilder {

        private String host;
        private String method;
        private String path;
        private String contentType;
        private String body;

        public RequestBuilder(){
            host = method = path = contentType = body = null;
        }

        public RequestBuilder setHost(String host){
            this.host = host;
            return this;
        }

        /**
         * sets the requeset to a GET request. 
         *
         * @param the body of the request to be sent to the server.
         *
         * @return this RequestBuilder
         *
         **/
        public RequestBuilder setGet() {
            this.method = "GET";
            return this;
        }

        /**
         * sets the requeset to a POST request. this requires a body to be sent to the
         * server.
         *
         * @param the body of the request to be sent to the server.
         *
         * @return this RequestBuilder
         *
         **/
        public RequestBuilder setPost(String body) {
            this.method = "POST";
            if(body == null)
                this.body = "";
            else
                this.body = body;
            return this;
        }

        /**
         * sets the requeset to a POST request. this requires a body to be sent to the
         * server.
         *
         * @param the body of the request to be sent to the server.
         *
         * @return this RequestBuilder
         *
         **/
        public RequestBuilder setPost(String body, String contentType) {
            this.method = "POST";
            if(body == null){
                this.body = "";
                this.contentType = "application/octet-stream";
            } else {
                this.body = body;
                if(contentType == null)
                    this.contentType = "application/octet-stream";
                else
                    this.contentType = contentType;
            }
            return this;
        }

        public RequestBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public Request build(){
            return new Request(this);
        }

    }

}
