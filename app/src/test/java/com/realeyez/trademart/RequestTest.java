package com.realeyez.trademart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.realeyez.trademart.encryption.Decryptor;
import com.realeyez.trademart.encryption.Encryptor;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.Content.ContentBuilder;
import com.realeyez.trademart.request.Request.RequestBuilder;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

public class RequestTest {

    // @Test
    public void test_getRequest(){
        Request request = new RequestBuilder()
            .setHost("127.0.0.1")
            .setGet()
            .setPath("/user/29123")
            .build();
        Response response = null;
        try {
            response = request.sendRequest();
        } catch (IOException e){
            Logger.log("Unable to send the request in test_getRequest()", LogLevel.CRITICAL);
        }
        System.out.printf("the received response code: %d, content: %s\n", response.getCode(), response.getContent());
        String expected = "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";

        assertEquals(expected, response.getContent());
    }

    @Test
    public void test_postRequest(){
        String password = "ThisPasswordIsPassable";
        Encryptor encryptor = new Encryptor();
        String saltIV = encryptor.getSaltIV();
        String encryptedPassword = encryptor.encrypt(password);

        Logger.log(new String("encryptedPassword: ").concat(encryptedPassword), LogLevel.INFO);

        Decryptor decryptor = new Decryptor(saltIV);
        String decryptedPassword = decryptor.decrypt(encryptedPassword);
        Logger.log(new String("decryptedPassword: ").concat(decryptedPassword), LogLevel.INFO);

        assertEquals(password, decryptedPassword);

        Content content = new ContentBuilder()
            .put("name", "From Test Client")
            .put("email", "testmail@test.tess")
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
        } catch (IOException e){
            Logger.log("Unable to send the request in test_postRequest()", LogLevel.CRITICAL);
        }
        System.out.printf("the received response code: %d, content: %s\n", response.getCode(), response.getContent());
        System.out.printf("redirect to: %s\n", response.getLocation());
        // String expected = "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";
        //
        // assertEquals(expected, response);
    }
}
