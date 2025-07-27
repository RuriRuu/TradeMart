package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.MessagingActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.RatingViewActivity;
import com.realeyez.trademart.job.JobItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class CompletedJobSheet extends BottomSheetDialogFragment {

    public static final String TAG = "CompletedJobSheet";

    private Button chatButton;
    private Button rateButton;

    private JobItem jobItem;

    public CompletedJobSheet(JobItem jobItem){
        this.jobItem = jobItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_completed_job_options, container, false);

        chatButton = panel.findViewById(R.id.completedjob_chat_button);
        rateButton = panel.findViewById(R.id.completedjob_rate_button);

        addOnClickListeners();
        return panel;
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
        rateButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), RatingViewActivity.class);
            intent.putExtra("transaction_id", jobItem.getTransactionId());
            startActivity(intent);
            dismiss();
        });
    }

}
