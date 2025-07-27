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
import com.realeyez.trademart.RatingViewActivity;
import com.realeyez.trademart.gui.components.job.JobItemPanelMixed;
import com.realeyez.trademart.gui.sheets.ActiveJobSheet;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ActiveTransactionsListFragment extends Fragment {
    
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

        FragmentManager fragman = requireActivity().getSupportFragmentManager();
        registerCompleteResultAction(fragman);

        reset(inflater);

        context = requireContext();
        return layout;
    }

    private void reset(LayoutInflater inflater){
        contentPanel.removeAllViews();
        loadActiveTransactions(inflater);
    }

    private void registerCompleteResultAction(FragmentManager fragman){
        fragman.setFragmentResultListener("complete_result", this, (key, result) -> {
            if(result.getBoolean("success")){
                reset(getLayoutInflater());
                Intent intent = new Intent(getContext(), RatingViewActivity.class);
                intent.putExtra("transaction_id", result.getInt("transaction_id"));
                startActivity(intent);
            }
        });
    }


    private void addPanel(LayoutInflater inflater, JobItemMixed jobItem){
        JobItemPanelMixed panel = JobItemPanelMixed.inflate(inflater, jobItem);
        panel.setOnJobItemClickedListener(item -> {
            jobItemClickedAction(item);
        });
        contentPanel.addView(panel);
    }

    private void jobItemClickedAction(JobItem jobItem){
        int userId = ResourceRepository.getResources().getCurrentUser().getId();
        if(userId == jobItem.getEmployeeId()){
            Intent intent = new Intent(getContext(), MessagingActivity.class);
            intent.putExtra("user_id", jobItem.getEmployerId());
            intent.putExtra("convo_id", -1);
            intent.putExtra("username", jobItem.getUsername());
            startActivity(intent);
            return;
        }
        ActiveJobSheet sheet = new ActiveJobSheet(jobItem);
        sheet.show(getParentFragmentManager(), ActiveJobSheet.TAG);
    }

    private void loadActiveTransactions(LayoutInflater inflater){
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
        ArrayList<JobItemMixed> activeJobs = new ArrayList<>();
        int userId = ResourceRepository.getResources().getCurrentUser().getId();
        Content content = new Content.ContentBuilder()
            .put("user_id", userId)
            .build();
        Response response = RequestUtil.sendPostRequest("/jobs/active", content);
        JSONObject responseJson = response.getContentJson();
        if(responseJson.getString("status").equals("failed")){
            String error = responseJson.getString("message");
            requireActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog(error, requireContext());
            });
            return null;
        }
        JSONArray activeJobsJson = responseJson.getJSONObject("data").getJSONArray("active_jobs");
        for (int i = 0; i < activeJobsJson.length(); i++) {
            JSONObject appJson = activeJobsJson.getJSONObject(i);
            JobTransactionType type = JobTransactionType.valueOf(appJson.getString("type"));
            int employeeId = appJson.getInt("employee_id");
            int employerId = appJson.getInt("employer_id");
            String displayName = null;
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
            activeJobs.add(new JobItemMixed.Builder()
                    .setTransactionId(appJson.getInt("id"))
                    .setEmployeeId(employeeId)
                    .setEmployerId(employerId)
                    .setType(type)
                    .setUsername(displayName)
                    .setTitle(appJson.getString("job_title"))
                    .setProfilePictureUri(pfpUri)
                    .build());
        }
        return activeJobs;
    }

}
