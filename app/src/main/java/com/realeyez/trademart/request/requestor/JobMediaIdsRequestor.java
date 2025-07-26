package com.realeyez.trademart.request.requestor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;

public class JobMediaIdsRequestor {

    public static ArrayList<Integer> sendRequest(int jobId)throws JSONException, FileNotFoundException, IOException{
        ArrayList<Integer> ids = new ArrayList<>();
        String path = new StringBuilder()
            .append("/jobs/")
            .append(jobId)
            .append("/media")
            .toString();
        Response response = RequestUtil.sendGetRequest(path);
        JSONArray arr = response.getContentJson().getJSONArray("media_ids");
        for (int i = 0; i < arr.length(); i++) {
            ids.add(arr.getInt(i));
        }
        return ids;

    }

}
