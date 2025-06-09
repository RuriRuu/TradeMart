package com.realeyez.trademart;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class JobListEditing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_job_list_editing), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton exitJobList = findViewById(R.id.exitJobListView);
        ImageButton reportJobList = findViewById(R.id.reportButton);

        exitJobList.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(JobListEditing.this, LoginPageActivity.class);
            startActivity(explicitActivity);
        });

        reportJobList.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.reporting_prompt);
            dialog.show();
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.findViewById(R.id.cancelReportPrompt).setOnClickListener(v -> dialog.dismiss());

        });

        Spinner JobTiers = findViewById(R.id.jobListTiers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.job_tiers,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_style);
        JobTiers.setAdapter(adapter);


    }
}
