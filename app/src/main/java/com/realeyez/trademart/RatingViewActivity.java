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
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.Slider;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

public class RatingViewActivity extends AppCompatActivity {

    private LinearLayout sliderContainer;
    private RatingSet rating;
    private Button submitRating;
    private ImageButton exitMenu;

    private int transactionId;

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

        Intent intent = getIntent();
        transactionId = intent.getIntExtra("transaction_id", -1);

        submitRating = findViewById(R.id.SubmitRating);
        exitMenu = findViewById(R.id.ExitRatingView);
        //Rating View > Home
        exitMenu.setOnClickListener(view -> {
            finish();
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
            label.setText(name.concat(": 2.5"));

            // Add change listener to update value dynamically
            Slider slider = sliderCard.findViewById(R.id.discreteSlider);
            slider.addOnChangeListener((s, value, fromUser) -> label.setText(String.format("%s: %.1f", name, (double) value)));

            // Add to container
            sliderContainer.addView(sliderCard);
        }

        submitRating.setOnClickListener(view -> {
            int childCount = sliderContainer.getChildCount();
            if (childCount < 4) return; // Safety check

            double communication = getSliderValue(sliderContainer.getChildAt(0));
            double speed = getSliderValue(sliderContainer.getChildAt(1));
            double quality = getSliderValue(sliderContainer.getChildAt(2));
            double value = getSliderValue(sliderContainer.getChildAt(3));

            rating = new RatingSet((float) communication, (float) speed, (float) quality, (float) value);
            System.out.println(rating);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {
                    if(sendRatingRequest(rating.getOverall())){
                        runOnUiThread(() -> {
                            showRating();
                        });
                        return;
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> Toast.makeText(this, "Unable to send rating!", Toast.LENGTH_LONG));
            });
        });

    }

    private boolean sendRatingRequest(double rating) throws JSONException, IOException{
        int raterId = ResourceRepository.getResources().getCurrentUser().getId();
        Content content = new Content.ContentBuilder()
            .put("transaction_id", transactionId)
            .put("rater_id", raterId)
            .put("rating",rating)
            .build();
        Response response = RequestUtil.sendPostRequest("/rate/jobs", content);
        JSONObject responseJson = response.getContentJson();
        if(responseJson.getString("status").equals("failed")){
            return false;
        }
        return true;
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


        dialog.setOnDismissListener((dialogInterface) -> {
            finish();
        });
        dialog.findViewById(R.id.closeButton).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

}
