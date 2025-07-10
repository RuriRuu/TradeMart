package com.realeyez.trademart;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import com.realeyez.trademart.encryption.Decryptor;
import com.realeyez.trademart.encryption.Encryptor;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.ContentDisposition;
import com.realeyez.trademart.request.ContentRange;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.Content.ContentBuilder;
import com.realeyez.trademart.request.Request.RequestBuilder;
import com.realeyez.trademart.util.Encoder;
import com.realeyez.trademart.util.FileUtil;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

public class RequestTest {

    // @Test
    public void test_getRequest() {
        Request request = new RequestBuilder()
                .setHost("127.0.0.1")
                .setGet()
                .setPath("/user/29123")
                .build();
        Response response = null;
        try {
            response = request.sendRequest();
        } catch (IOException e) {
            Logger.log("Unable to send the request in test_getRequest()", LogLevel.CRITICAL);
        }
        System.out.printf("the received response code: %d, content: %s\n", response.getCode(), response.getContent());
        String expected = "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";

        assertEquals(expected, response.getContent());
    }

    // @Test
    public void test_postRequest() {
        String password = "InsanelyFunnyPassword";
        Encryptor encryptor = new Encryptor();
        String saltIV = encryptor.getSaltIV();
        String encryptedPassword = encryptor.encrypt(password);

        Logger.log(new String("encryptedPassword: ").concat(encryptedPassword), LogLevel.INFO);

        Decryptor decryptor = new Decryptor(saltIV);
        String decryptedPassword = decryptor.decrypt(encryptedPassword);
        Logger.log(new String("decryptedPassword: ").concat(decryptedPassword), LogLevel.INFO);

        assertEquals(password, decryptedPassword);
        Content content = new ContentBuilder()
                .put("username", "UserTwo")
                .put("email", "seconduser@funny.haha")
                .put("password", encryptedPassword)
                .put("salt_iv", saltIV)
                .build();
        Logger.log(new String("content to be sent: ").concat(content.getContentString()), LogLevel.INFO);

        Request request = new RequestBuilder()
                .useSSL()
                .setPost(content.getContentString())
                .setHost("thinkpad-x230.taila38b71.ts.net")
                .noPort()
                .setPath("/user/signup")
                .build();
        Response response = null;
        try {
            response = request.sendRequest();
        } catch (IOException e) {
            Logger.log("Unable to send the request in test_postRequest()", LogLevel.CRITICAL);
        }
        if (response == null) {
            System.out.printf("Unable to reach the server\n");
            return;
        }
        System.out.printf("the received response code: %d, content: %s\n", response.getCode(), response.getContent());
        System.out.printf("redirect to: %s\n", response.getLocation());
        // String expected =
        // "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";
        //
        String singupStatus = null;
        try {
            singupStatus = response.getContentJson().getString("status");
        } catch (JSONException e) {
        }
        assertEquals("success", singupStatus);
    }

    // @Test
    public void test_Login() {
        String password = "ThisPasswordIsPassable";
        Encryptor encryptor = new Encryptor();
        String saltIV = encryptor.getSaltIV();
        String encryptedPassword = encryptor.encrypt(password);

        Content content = new ContentBuilder()
                .put("username", "RedFlameKen")
                .put("password", encryptedPassword)
                .put("salt_iv", saltIV)
                .build();

        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/user/login", content);
        } catch (IOException e) {
            Logger.log("Unable to send response", LogLevel.WARNING);
        }
        System.out.printf("received response for login: code: %d, content: %s\n", response.getCode(),
                response.getContent());
        JSONObject json = null;
        String responseStatus = "";
        try {
            json = new JSONObject(new JSONTokener(response.getContent()));
            responseStatus = json.getString("status");
        } catch (JSONException e){
            Logger.log("response was badly formatted", LogLevel.WARNING);
        }
        assertEquals("success", responseStatus);
    }

    // @Test
    public void test_post(){
        String title = "cool post";
        String description = "Ok, I don't know about you, but TradeMart is awesome!";
        int userId = 32378;
        Content content = new Content.ContentBuilder()
            .put("title", title)
            .put("description", description)
            .put("user_id", userId)
            .build();
        Response response = null;
        try {
            response = RequestUtil.sendPostRequest("/post/publish", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String r_title = null;
        String r_description = null;
        int r_userId = 0;
        int r_postId = 0;
        System.out.printf("received response: %s\n", response.getContent());
        try {
            JSONObject json = response.getContentJson();
            r_title = json.getString("title");
            r_description = json.getString("description");
            r_userId = json.getInt("user_id");
            r_postId = json.getInt("post_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.printf("post_id: %s\n", r_postId);
        assertEquals(title, r_title);
        assertEquals(description, r_description);
        assertEquals(userId, r_userId);
    }

    // @Test
    public void test_fetchMedia(){
        Response response = null;
        try {
            response = RequestUtil.sendGetRequest("/media/93490");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("received response: %s\n", response.getContent());
        String filename = null;
        String encodedData = null;
        try {
            JSONObject json = response.getContentJson();
            filename = json.getString("filename");
            encodedData = json.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        File file = new File(".tempfile-".concat(filename));
        try (FileOutputStream outstream = new FileOutputStream(file)) {
            byte[] bytes = Encoder.decodeBase64(encodedData);
            outstream.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @Test
    public void test_contentRange(){
        String rangeString = "bytes 100-278/448";
        ContentRange range = ContentRange.parse(rangeString);
        assertEquals(100, range.getStart());
        assertEquals(278, range.getEnd());
        assertEquals(448, range.getSize());
    }

    // @Test
    public void test_contentRangeNoSize(){
        String rangeString = "bytes 100-278";
        ContentRange range = ContentRange.parse(rangeString);
        assertEquals(100, range.getStart());
        assertEquals(278, range.getEnd());
        assertEquals(0, range.getSize());
    }

    // @Test
    public void test_byteStringConversion(){
        String text = "Hello, world!";
        byte[] bytes = text.getBytes();
        assertEquals(text, new String(bytes));
    }

    // @Test
    public void test_hlsVideoUpload() throws IOException, JSONException{
        File file = new File("/home/redflameken/Videos/memes/fallguys_battlepass.mp4");
        byte[] bytes = null;
        try (FileInputStream reader = new FileInputStream(file)) {
            bytes = reader.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.log("error here breh", LogLevel.INFO);
        }
        String encodedData = Encoder.encodeBase64(bytes);
        Content content = new Content.ContentBuilder()
            .put("filename", file.getName())
            .put("data", encodedData)
            .build();

        Response response = RequestUtil.sendPostRequest("/post/publish/95704/media", content);
        JSONObject json = response.getContentJson();
        String status = json.getString("status");
        assertEquals(status, "success");
    }

    // @Test
    public void test_contentDisposition() throws IOException {
        Response response = RequestUtil.sendGetRequest("/media/image/god_totem.png");
        String filename = response.getContentDispositionField("filename");
        System.out.printf("filename: %s\n", filename);
        assertEquals("god_totem.png", filename);

    }

    // @Test
    public void test_disposition() throws IOException {
        String content = "blabby blabby blabyy";
        String filename = "silly_goobus.txt";
        ContentDisposition disposition = ContentDisposition.attachment()
            .addDisposition("filename", filename);
        Response response = RequestUtil.sendPostRequest("/disposition", content.getBytes(), disposition);
        String rString = response.getContent();
        System.out.printf("%s\n", rString);
        assertEquals(filename, response.getContentDispositionField("filename"));
        assertEquals(content, response.getContent());
    }

    // @Test
    public void test_profilePictureUpload(){

        String filepath = "/home/redflameken/Pictures/RedFlameKen.jpg";
        ContentDisposition disposition = ContentDisposition.attachment()
            .addDisposition("filename", "RedFlameKen.jpg");
        byte[] data = FileUtil.readFile(filepath);
        for (byte b : data) {
            System.out.print(b);
        }
        String path = new StringBuilder()
            .append("/user/")
            .append(84335)
            .append("/avatar/update")
            .toString();
        try {
            Response response = RequestUtil.sendPostRequest(path, data, disposition);
            Logger.log("status: " + response.getCode(), LogLevel.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] result = FileUtil.readFile("/home/redflameken/Storage/media/images/pfp_84335.jpg");
    }

}
