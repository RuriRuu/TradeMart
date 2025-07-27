package com.realeyez.trademart.gui.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.job.JobItemPanelMixed;
import com.realeyez.trademart.gui.components.job.event.OnJobItemClickedListener;
import com.realeyez.trademart.gui.sheets.CompletedJobSheet;
import com.realeyez.trademart.job.JobItem;
import com.realeyez.trademart.job.JobItemMixed;
import com.realeyez.trademart.job.JobTransactionType;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.requestor.ProfilePictureRequestor;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CompletedTransactionsListFragment extends Fragment {
    
    private LinearLayout contentPanel;
    private SwipeRefreshLayout refresh;

    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_job_transaction_list, null);
        contentPanel = layout.findViewById(R.id.jobtransactionslist_content_panel);
        refresh = layout.findViewById(R.id.jobtransactionslist_refresh);

        refresh.setOnRefreshListener(() -> {
            reset(inflater);
        });

        reset(inflater);

        context = requireContext();
        return layout;
    }

    private void reset(LayoutInflater inflater){
        contentPanel.removeAllViews();
        loadCompletedTransactions(inflater);
    }

    private void addPanel(LayoutInflater inflater, JobItemMixed jobItem){
        JobItemPanelMixed panel = JobItemPanelMixed.inflate(inflater, jobItem);
        panel.setOnJobItemClickedListener(item -> {
            onPanelClickedAction(item);
        });
        contentPanel.addView(panel);
    }

    private boolean sendCheckRatedRequest(JobItem jobItem)throws FileNotFoundException, IOException, JSONException{
        String path = new StringBuilder()
            .append("/rate/rated/transaction/")
            .append(jobItem.getTransactionId())
            .toString();
        Response response = RequestUtil.sendGetRequest(path);
        return response.getContentJson().getBoolean("rated");

    }

    private void onPanelClickedAction(JobItem jobItem){
        int userId = ResourceRepository.getResources().getCurrentUser().getId();
        if(userId == jobItem.getEmployeeId()){
            showMessagingActivity(jobItem);
            return;
        }
        Activity activity = requireActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            boolean rated = false;
            try {
                rated = sendCheckRatedRequest(jobItem);
            } catch(JSONException | IOException e){
                e.printStackTrace();
                activity.runOnUiThread(() -> Toast.makeText(context, "An error occured", Toast.LENGTH_SHORT));
                return;
            }
            if(!rated)
                activity.runOnUiThread(() -> showSheet(jobItem));
            else 
                activity.runOnUiThread(() -> showMessagingActivity(jobItem));
        });
    }

    private void showMessagingActivity(JobItem jobItem){
        Intent intent = new Intent(getContext(), MessagingActivity.class);
        intent.putExtra("user_id", jobItem.getEmployerId());
        intent.putExtra("convo_id", -1);
        intent.putExtra("username", jobItem.getUsername());
        startActivity(intent);
        return;
    }

    private void showSheet(JobItem jobItem){
        CompletedJobSheet sheet = new CompletedJobSheet(jobItem);
        sheet.show(getParentFragmentManager(), CompletedJobSheet.TAG);
    }

    private void loadCompletedTransactions(LayoutInflater inflater){
        Activity activity = requireActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            try {
                ArrayList<JobItemMixed> items = sendFetchRequest();
                activity.runOnUiThread(() -> {
                    for (JobItemMixed item : items) {
                        addPanel(inflater, item);
                    }
                    refresh.setRefreshing(false);
                });
            } catch (JSONException | IOException e){
                e.printStackTrace();
                    refresh.setRefreshing(false);
            }
        });
    }

    private ArrayList<JobItemMixed> sendFetchRequest() throws FileNotFoundException, IOException, JSONException{
        ArrayList<JobItemMixed> completedTransactions = new ArrayList<>();
        int userId = ResourceRepository.getResources().getCurrentUser().getId();
        Content content = new Content.ContentBuilder()
            .put("user_id", userId)
            .build();
        Response response = RequestUtil.sendPostRequest("/jobs/completed", content);
        JSONObject responseJson = response.getContentJson();
        if(responseJson.getString("status").equals("failed")){
            String error = responseJson.getString("message");
            requireActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog(error, requireContext());
            });
            return null;
        }
        JSONArray completedTransactionsJson = responseJson.getJSONObject("data").getJSONArray("completed_transactions");
        for (int i = 0; i < completedTransactionsJson.length(); i++) {
            JSONObject appJson = completedTransactionsJson.getJSONObject(i);
            JobTransactionType type = JobTransactionType.valueOf(appJson.getString("type"));
            int employeeId = appJson.getInt("employee_id");
            int employerId = appJson.getInt("employer_id");
            String displayName = "";
            int displayUserId = -1;
            if(userId == employeeId){ 
                displayName = appJson.getString("employer_username");
                displayUserId = employerId;
            } else if(userId == employerId){
                displayName = appJson.getString("employee_username");
                displayUserId = employeeId;
            }
            Uri pfpUri = null;
            try {
                pfpUri = ProfilePictureRequestor.sendRequest(displayUserId, context.getCacheDir());
            } catch (Exception e){
                e.printStackTrace();
            }
            completedTransactions.add(new JobItemMixed.Builder()
                    .setEmployeeId(employeeId)
                    .setEmployerId(employerId)
                    .setType(type)
                    .setTransactionId(appJson.getInt("id"))
                    .setUsername(displayName)
                    .setTitle(appJson.getString("job_title"))
                    .setProfilePictureUri(pfpUri)
                    .build());
        }
        return completedTransactions;
    }

}
