package com.realeyez.trademart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InterestViewActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.interestview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        linearLayout = findViewById(R.id.interest_container);
        String[] namesArray = getResources().getStringArray(R.array.job_categories);
        categories = new ArrayList<>(Arrays.asList(namesArray));
        createRandomButtons();
    }

    private void createRandomButtons() {
        Collections.shuffle(categories);
        List<String> selected = categories.subList(0, Math.min(5, categories.size()));

        for (String category : selected) {
            View buttonLayout = LayoutInflater.from(this).inflate(R.layout.button_generic, linearLayout, false);
            Button button = buttonLayout.findViewById(R.id.genericButton);
            button.setText(category);
            linearLayout.addView(buttonLayout);

        }
    }
}