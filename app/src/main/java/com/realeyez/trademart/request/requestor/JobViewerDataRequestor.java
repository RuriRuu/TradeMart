package com.realeyez.trademart.request.requestor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.model.profile.JobViewerData;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.user.User;

public class JobViewerDataRequestor {

    public static JobViewerData sendRequest(int jobId, int employerId) throws JSONException, FileNotFoundException, IOException{
        JobViewerData jobViewerData = null;
        String path = new StringBuilder()
            .append("/jobs/find/")
            .append(jobId).toString();

        Response response = RequestUtil.sendGetRequest(path);
        JSONObject responseJson = response.getContentJson();
        if(responseJson.getString("status").equals("failed")){
            return null;
        }
        JSONObject data = responseJson.getJSONObject("data");
        User user = UserRequestor.sendRequest(employerId);
        ArrayList<Integer> mediaIds = JobMediaIdsRequestor.sendRequest(jobId);

        jobViewerData = new JobViewerData.Builder()
            .setJobId(data.getInt("job_id"))
            .setUsername(user.getUsername())
            .setMediaIds(mediaIds)
            .build();
        return jobViewerData;

    }
    
}
