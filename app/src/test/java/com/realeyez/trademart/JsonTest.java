package com.realeyez.trademart;

import static org.junit.Assert.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.ContentArray;

public class JsonTest {

    @Test
    public void test_JSONArray(){
        JSONArray arr = new JSONArray();
        arr.put("cat");
        arr.put("dog");
        arr.put("platypus");

        JSONObject obj = new JSONObject();
        try {
            obj.put("arr", arr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.printf("object data: %s\n", obj.toString());
    }

    @Test
    public void test_ContentArray(){
        String expected = "{\"arr\":[\"cat\",\"dog\",\"platypus\"]}";
        ContentArray arr = new ContentArray.ContentArrayBuilder()
            .put("cat")
            .put("dog")
            .put("platypus")
            .build();

        Content content = new Content.ContentBuilder()
            .put("arr", arr)
            .build();

        assertEquals(expected, content.getContentString());
    }
}
