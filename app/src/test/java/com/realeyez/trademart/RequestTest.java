package com.realeyez.trademart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.realeyez.trademart.encryption.Decryptor;
import com.realeyez.trademart.encryption.Encryptor;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.Content.ContentBuilder;
import com.realeyez.trademart.request.Request.RequestBuilder;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

public class RequestTest {

    public void test_getRequest(){
        Request request = new RequestBuilder()
            .setHost("127.0.0.1")
            .setGet()
            .setPath("/user/29123")
            .build();
        String response = request.sendRequest();
        System.out.printf("the received response: %s\n", response);
        String expected = "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";

        assertEquals(expected, response);
    }

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
            .setHost("127.0.0.1")
            .setPost(content.getContentString())
            .setPath("/user/signup")
            .build();

        String response = request.sendRequest();
        System.out.printf("the received response: %s\n", response);
        // String expected = "{\"id\":29123,\"name\":\"RedFlameKen\",\"email\":\"example@mail.com\",\"password\":null}";
        //
        // assertEquals(expected, response);
    }
}
