package com.realeyez.trademart.gui.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.job.JobItemPanel;
import com.realeyez.trademart.gui.sheets.HiringsJobSheet;
import com.realeyez.trademart.gui.sheets.ProfilePictureSheet;
import com.realeyez.trademart.job.JobItem;
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

public class HiringsListFragment extends Fragment {
    
    private LinearLayout contentPanel;
    private SwipeRefreshLayout refresh;

    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_job_transaction_list, null);
        contentPanel = layout.findViewById(R.id.jobtransactionslist_content_panel);
        refresh = layout.findViewById(R.id.jobtransactionslist_refresh);

        context = requireContext();

        refresh.setOnRefreshListener(() -> {
            reset(inflater);
        });

        FragmentManager fragman = requireActivity().getSupportFragmentManager();
        registerHireResultAction(fragman);

        reset(inflater);

        return layout;
    }

    private void reset(LayoutInflater inflater){
        contentPanel.removeAllViews();
        loadHirings(inflater);
    }

    private void registerHireResultAction(FragmentManager fragman){
        fragman.setFragmentResultListener("hire_result", this, (key, result) -> {
            if(result.getBoolean("success")){
                reset(getLayoutInflater());
            }
        });
    }

    private void addPanel(LayoutInflater inflater, JobItem jobItem){
        JobItemPanel panel = JobItemPanel.inflate(inflater, jobItem);

        panel.setOnJobItemClickedListener((item) -> {
            HiringsJobSheet sheet = new HiringsJobSheet(jobItem);
            sheet.show(getParentFragmentManager(), HiringsJobSheet.TAG);
        });
        contentPanel.addView(panel);
    }

    private void loadHirings(LayoutInflater inflater){
        Activity activity = requireActivity();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(() -> {
            try {
                ArrayList<JobItem> items = sendFetchRequest();
                activity.runOnUiThread(() -> {
                    for (JobItem item : items) {
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

    private ArrayList<JobItem> sendFetchRequest() throws FileNotFoundException, IOException, JSONException{
        ArrayList<JobItem> hirings = new ArrayList<>();
        int employerId = ResourceRepository.getResources().getCurrentUser().getId();
        Content content = new Content.ContentBuilder()
            .put("employer_id", employerId)
            .build();
        Response response = RequestUtil.sendPostRequest("/jobs/hirings", content);
        JSONObject responseJson = response.getContentJson();
        if(responseJson.getString("status").equals("failed")){
            String error = responseJson.getString("message");
            requireActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog(error, requireContext());
            });
            return null;
        }
        JSONArray hiringsJson = responseJson.getJSONObject("data").getJSONArray("hirings");
        for (int i = 0; i < hiringsJson.length(); i++) {
            JSONObject appJson = hiringsJson.getJSONObject(i);
            int employeeId = appJson.getInt("employee_id");
            Uri pfpUri = null;
            try {
                pfpUri = ProfilePictureRequestor.sendRequest(employeeId, context.getCacheDir());
            } catch (Exception e){
                e.printStackTrace();
            }
            hirings.add(new JobItem.Builder()
                    .setTransactionId(appJson.getInt("id"))
                    .setEmployeeId(employeeId)
                    .setEmployerId(employerId)
                    .setTransactionId(appJson.getInt("id"))
                    .setUsername(appJson.getString("employee_username"))
                    .setTitle(appJson.getString("job_title"))
                    .setProfilePictureUri(pfpUri)
                    .build());
        }
        return hirings;
    }

}
