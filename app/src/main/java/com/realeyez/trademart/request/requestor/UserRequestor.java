package com.realeyez.trademart.request.requestor;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.user.User;

public class UserRequestor {

    public static User sendRequest(int userId)
            throws JSONException, FileNotFoundException, IOException {
        String path = new StringBuilder()
            .append("/user/profile/")
            .append(userId)
            .toString();
        Response response = RequestUtil.sendGetRequest(path);
        JSONObject json = response.getContentJson();
        return new User.UserBuilder()
            .setId(json.getInt("user_id"))
            .setUsername(json.getString("username"))
            .setEmail(json.getString("email"))
            .build();
    }
    
}
