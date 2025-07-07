package com.realeyez.trademart.request;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.realeyez.trademart.request.Response.ResponseBuilder;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.net.Uri;

public class Request {

    public static final String DEFAULT_SERVER_HOST = "10.0.2.2";
    public static final String DEFAULT_SERVER_HOST_SSL = "thinkpad-x230.taila38b71.ts.net";
    public static final int DEFAULT_SERVER_PORT = 8080;

    private String host;
    private int port;
    private String method;
    private String path;
    private byte[] body;
    private String contentType;
    private long contentLength;
    private ContentRange contentRange;
    private ContentDisposition disposition;

    private boolean usingSSL;
    private URLConnection con;

    public Request(RequestBuilder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.method = builder.method;
        this.path = builder.path;
        this.body = builder.body;
        this.contentLength = builder.contentLength;
        this.contentType = builder.contentType;
        this.contentRange = builder.contentRange;
        this.disposition = builder.disposition;
        this.usingSSL = builder.usingSSL;
    }

    /**
     * sends the built request to the server, then returns a string containing the
     * response body sent by the server.
     *
     *
     * @return response from the server
     *
     **/
    public Response sendRequest() throws IOException {
        return sendHttpRequest();
    }

    public Uri getUri(){
        return Uri.parse(buildURL());
    }

    private Response sendHttpRequest() throws IOException {

        URL url = new URL(buildURL());
        con = (HttpURLConnection) url.openConnection();
        if (usingSSL)
            ((HttpsURLConnection) con).setRequestMethod(method);
        else
            ((HttpURLConnection) con).setRequestMethod(method);
        con.setRequestProperty("Content-Type", contentType);
        if(disposition != null){
            con.setRequestProperty("Content-Disposition", disposition.getHeader());
        }
        // HttpParams params = con.getPar
        // con.getHeaderField("Content");
        if (method.equals("POST")) {
            con.setDoOutput(true);
        }
        con.setDoInput(true);

        if (method.equals("POST")) {
            sendContentBody();
        }
        Response response = readResponse();
        if (usingSSL)
            ((HttpsURLConnection) con).disconnect();
        else
            ((HttpURLConnection) con).disconnect();
        return response;

    }

    private void sendContentBody() {
        try (OutputStream os = con.getOutputStream()) {
            os.write(body);
        } catch (IOException e) {
            Logger.log("IOException when writing request", LogLevel.CRITICAL);
            e.printStackTrace();
        }
    }

    private Response readResponse() {
        Response response = null;
        try {
            ContentRange contentRange = ContentRange.parse(con.getHeaderField("Content-Range"));
            byte[] content = null;
            int contentLength = con.getHeaderFieldInt("Content-Length", 0);
            ResponseBuilder builder = new ResponseBuilder()
                    .setCode(usingSSL == true ? ((HttpsURLConnection) con).getResponseCode()
                            : ((HttpURLConnection) con).getResponseCode())
                    .setLocation(con.getHeaderField("Location"))
                    .setHost(createResponseHost())
                    .setContentLength(contentLength)
                    .setContentDisposition(con.getHeaderField("Content-Disposition"))
                    .setContentType(con.getContentType());
            if(contentRange == null){
                content = readAllResponseBytes(contentLength);
            } else {
                builder.setContentRange(contentRange);
                content = readNResponseBytes(
                        (int) contentRange.getStart(),
                        (int) contentRange.getEnd(),
                        contentLength
                        );
            }
            builder.setContent(content);
            response = builder.build();
        } catch (IOException e) {
            Logger.log("Unable to read response", LogLevel.WARNING);
            e.printStackTrace();
        }
        return response;
    }

    private byte[] readNResponseBytes(int start, int end, int length){
        byte[] bytes = new byte[length];
        try (BufferedInputStream reader = new BufferedInputStream(con.getInputStream())) {
            reader.read(bytes, start, end);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private byte[] readAllResponseBytes(int length){
        byte[] bytes = new byte[length];
        try (BufferedInputStream reader = new BufferedInputStream(con.getInputStream())) {
            int totalRead = 0;
            while(totalRead < length){
                int read = reader.read(bytes, totalRead, length-totalRead);
                if(read == -1) break;
                totalRead += read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private String readResponseBody() {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Logger.log("IOException when reading response", LogLevel.CRITICAL);
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String buildURL() {
        StringBuilder builder = new StringBuilder()
                .append(getLocationBase())
                .append(getPortString())
                .append(path);
        return builder.toString();
    }

    private String createResponseHost() {
        StringBuilder builder = new StringBuilder()
                .append(usingSSL ? "https://" : "http://")
                .append(host == null ? 
                        usingSSL ?
                        DEFAULT_SERVER_HOST_SSL :
                        DEFAULT_SERVER_HOST 
                        : host)
                .append(getPortString());
        return builder.toString();
    }

    private String getLocationBase() {
        StringBuilder builder = new StringBuilder()
                .append(usingSSL ? "https://" : "http://")
                .append(host == null ? DEFAULT_SERVER_HOST : host);
        return builder.toString();
    }

    private String getPortString() {
        if (port == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder()
                .append(":")
                .append(port);
        return builder.toString();
    }

    public static class RequestBuilder {

        private String host;
        private int port;
        private String method;
        private String path;
        private String contentType;
        private long contentLength;
        private byte[] body;
        private boolean usingSSL;
        private ContentRange contentRange;
        private ContentDisposition disposition;

        public RequestBuilder() {
            host = method = path = contentType = null;
            body = null;
            usingSSL = false;
            port = DEFAULT_SERVER_PORT;
            contentLength = 0;
            contentRange = null;
            disposition = null;
        }

        public RequestBuilder setHost(String host) {
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
         * Make the request url not include a port. This just sets the port to -1 so
         * make sure to use this along with {@link RequestBuilder#setPort(int)
         * setPort()}
         * <br>
         *
         * This method should be used when you need to format a url to not include a
         * port (typically when you use a proxy that forwards a port). For example,
         * without calling noPort() while trying to connect to 127.0.0.1, the output url
         * would be http://127.0.0.1:8080/. Calling noPort() will format the url as
         * http://127.0.0.1/
         *
         *
         * @return this RequestBuilder
         *
         */
        public RequestBuilder noPort() {
            this.port = -1;
            return this;
        }

        public RequestBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Make the request use https instead of http.
         *
         * @return this RequestBuilder
         *
         */
        public RequestBuilder useSSL() {
            this.usingSSL = true;
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
        public RequestBuilder setPost(byte[] body) {
            this.method = "POST";
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
        public RequestBuilder setPost(String body) {
            this.method = "POST";
            if (body == null)
                this.body = "".getBytes();
            else
                this.body = body.getBytes();
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
            if (body == null) {
                this.body = "".getBytes();
                this.contentType = "application/octet-stream";
            } else {
                this.body = body.getBytes();
                if (contentType == null)
                    this.contentType = "application/octet-stream";
                else
                    this.contentType = contentType;
            }
            return this;
        }

        /**
         * sets the content type of the content body. To see all possible http types,
         * check {@see <a href="https://mimetype.io/all-types">mimetype.io</a>}
         *
         * @param contentType type of the content body
         *
         * @return this RequestBuilder
         */
        public RequestBuilder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestBuilder setContentLength(long contentLength){
            this.contentLength = contentLength;
            return this;
        }

        public RequestBuilder setContentRange(long start, long end, long size){
            this.contentRange = new ContentRange(start, end, size);
            return this;
        }

        public RequestBuilder setContentRange(long start, long end){
            this.contentRange = new ContentRange(start, end);
            return this;
        }

        public RequestBuilder setContentDisposition(ContentDisposition disposition) {
            this.disposition = disposition;
            return this;
        }

        public RequestBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
