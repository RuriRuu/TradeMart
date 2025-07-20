package com.realeyez.trademart;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;

import java.io.IOException;

public class JobListEditing extends AppCompatActivity {

    Button messageButton;
    ImageButton reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_list_editing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_job_list_editing), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messageButton = findViewById(R.id.JLmessageButton);
        reportButton = findViewById(R.id.reportButton);

        Spinner JobTiers = findViewById(R.id.jobListTiers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.job_tiers,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_style);
        JobTiers.setAdapter(adapter);

        messageButton.setOnClickListener(view -> messageButtonAction());

        reportButton.setOnClickListener(view -> reportButtonAction());

    }

    private void messageButtonAction() {


        //Message Service Connection VV
    }

    private void reportButtonAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(JobListEditing.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reporting_prompt, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        ImageButton cancelReport = dialogView.findViewById(R.id.cancelReportPrompt);
        EditText reportContext = dialogView.findViewById(R.id.reportReasonEditText);
        Button submitReport = dialogView.findViewById(R.id.submitReportButton);

        cancelReport.setOnClickListener(view -> dialog.dismiss());

        submitReport.setOnClickListener(view -> {

            String reportText = reportContext.getText().toString().trim();
            int serviceId = 0; //I don't know how to get the service id

            if (!reportText.isEmpty()) {
                Toast.makeText(JobListEditing.this, "Report sent: " + reportText, Toast.LENGTH_SHORT).show();
                sendReportActivity(reportText, serviceId);
                dialog.dismiss();
            } else {
                Toast.makeText(JobListEditing.this, "Please enter a report before sending.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Response sendReportActivity(String reportText, int serviceId) {

        Content content = new Content.ContentBuilder()
                .put("reportText", reportText)
                .put("serviceId", serviceId)
                .build();

        Response response = null;

        try {
            response = RequestUtil.sendPostRequest("/job/report", content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
