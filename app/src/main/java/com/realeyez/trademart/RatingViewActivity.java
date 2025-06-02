package com.realeyez.trademart;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RatingViewActivity extends AppCompatActivity {

    LinearLayout sliderContainer;
    RatingSet rating;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ratingview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton ExitMenu = findViewById(R.id.ExitRatingView);
        Button SubmitRating = findViewById(R.id.SubmitRating);


        //Rating View > Home
        ExitMenu.setOnClickListener(view -> {
            Intent explicitActivity = new Intent(RatingViewActivity.this, MainActivity.class);
            startActivity(explicitActivity);
        });

        //Set Slider
        sliderContainer = findViewById(R.id.slider_container);
        List<String> sliderNames = Arrays.asList("Communication Ability", "Speed of Transaction", "Quality of Product", "Value for Price");
        for (int i = 0; i < sliderNames.size(); i++) {
            String name = sliderNames.get(i);

            // Inflate slider_card.xml
            View sliderCard = LayoutInflater.from(this).inflate(R.layout.slider, sliderContainer, false);

            // Update the label
            TextView label = sliderCard.findViewById(R.id.Slider_Value);
            label.setText(name + ": 2.5");

            // Add change listener to update value dynamically
            Slider slider = sliderCard.findViewById(R.id.discreteSlider);
            slider.addOnChangeListener((s, value, fromUser) -> label.setText(name + ": " + (double) value));

            // Add to container
            sliderContainer.addView(sliderCard);
        }

        SubmitRating.setOnClickListener(view -> {
            int childCount = sliderContainer.getChildCount();
            if (childCount < 4) return; // Safety check

            double communication = getSliderValue(sliderContainer.getChildAt(0));
            double speed = getSliderValue(sliderContainer.getChildAt(1));
            double quality = getSliderValue(sliderContainer.getChildAt(2));
            double value = getSliderValue(sliderContainer.getChildAt(3));

            rating = new RatingSet((float) communication, (float) speed, (float) quality, (float) value);
            System.out.println(rating);

            showRating();
        });

    }

    private double getSliderValue(View sliderCard) {
        Slider slider = sliderCard.findViewById(R.id.discreteSlider);
        return slider.getValue();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void showRating() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rating_confirm);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextView ratingNumber = dialog.findViewById(R.id.ratingTextNumber);
        ratingBar.setRating(rating.getOverall());
        ratingNumber.setText(String.format("%.1f", rating.getOverall()));


        dialog.findViewById(R.id.closeButton).setOnClickListener(v -> dialog.dismiss());
    }

}
