package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.CreatePostActivity;
import com.realeyez.trademart.CreateServiceActivity;
import com.realeyez.trademart.R;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomepageCreateSheet extends BottomSheetDialogFragment {

    public static final String TAG = "HomepageCreateSheet";

    private Button postButton;
    private Button serviceButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_create_option_sheet, container, false);

        postButton = panel.findViewById(R.id.createoption_post_button);
        serviceButton = panel.findViewById(R.id.createoption_service_button);

        addOnClickListeners();
        return panel;
    }

    private void addOnClickListeners(){
        postButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CreatePostActivity.class);
            startActivity(intent);
        });
        serviceButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CreateServiceActivity.class);
            startActivity(intent);
        });
    }
    
}
