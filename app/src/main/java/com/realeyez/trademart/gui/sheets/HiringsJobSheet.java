package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.job.JobItem;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

public class HiringsJobSheet extends BottomSheetDialogFragment {

    public static final String TAG = "HiringsJobSheet";

    private Button chatButton;
    private Button hireButton;

    private JobItem jobItem;

    private FragmentActivity activity;

    public HiringsJobSheet(JobItem jobItem){
        this.jobItem = jobItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_hirings_job_options, container, false);

        chatButton = panel.findViewById(R.id.hiringjob_chat_button);
        hireButton = panel.findViewById(R.id.hiringjob_hire_button);

        addOnClickListeners();
        return panel;
    }

    private boolean sendHireRequest() throws JSONException, FileNotFoundException, IOException{
        Content content = new Content.ContentBuilder()
            .put("transaction_id", jobItem.getTransactionId())
            .build();
        Response response = RequestUtil.sendPostRequest("/jobs/hire", content);
        JSONObject responseJson = response.getContentJson();
        String message = responseJson.getString("message");
        activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_LONG));
        if(responseJson.getString("status").equals("failed")){
            return false;
        }
        return true;
    }

    private void addOnClickListeners(){
        chatButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MessagingActivity.class);
            intent.putExtra("user_id", jobItem.getEmployeeId());
            intent.putExtra("convo_id", -1);
            intent.putExtra("username", jobItem.getUsername());
            startActivity(intent);
            dismiss();
        });
        hireButton.setOnClickListener(view -> {
            activity = getActivity();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Bundle result = new Bundle();
                try {
                    boolean success = sendHireRequest();
                    result.putBoolean("success", success);
                } catch (JSONException | IOException e){
                    e.printStackTrace();
                    result.putBoolean("success", false);
                }
                activity.runOnUiThread(() -> activity.getSupportFragmentManager().setFragmentResult("hire_result", result));
            });
            dismiss();
        });
    }

}
