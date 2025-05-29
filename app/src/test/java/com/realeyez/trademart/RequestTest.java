package com.realeyez.trademart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.Request.RequestBuilder;

public class RequestTest {

    @Test
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

}
