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

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;

public class ActiveJobSheet extends BottomSheetDialogFragment {

    public static final String TAG = "ActiveJobSheet";

    private Button chatButton;
    private Button completeButton;

    private JobItem jobItem;
    private FragmentActivity activity;

    public ActiveJobSheet(JobItem jobItem){
        this.jobItem = jobItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_active_job_options, container, false);

        chatButton = panel.findViewById(R.id.activejob_chat_button);
        completeButton = panel.findViewById(R.id.activejob_finish_button);

        addOnClickListeners();
        return panel;
    }

    private boolean sendCompleteRequest() throws JSONException, FileNotFoundException, IOException{
        Content content = new Content.ContentBuilder()
            .put("transaction_id", jobItem.getTransactionId())
            .build();
        Response response = RequestUtil.sendPostRequest("/jobs/complete", content);
        JSONObject responseJson = response.getContentJson();
        String message = responseJson.getString("message");
        activity.runOnUiThread(() -> Toast.makeText(activity, message, Toast.LENGTH_LONG).show());
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
        });
        completeButton.setOnClickListener(view -> {
            activity = requireActivity();
            activity = getActivity();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Bundle result = new Bundle();
                try {
                    boolean success = sendCompleteRequest();
                    result.putBoolean("success", success);
                } catch (JSONException | IOException e){
                    e.printStackTrace();
                    result.putBoolean("success", false);
                }
                activity.runOnUiThread(() -> activity.getSupportFragmentManager().setFragmentResult("complete_result", result));
            });
            dismiss();
        });
    }

}
