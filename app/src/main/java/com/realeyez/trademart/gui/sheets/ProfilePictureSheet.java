package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.CreatePostActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.media.MediaPicker;
import com.realeyez.trademart.profile.ProfilePictureUpdater;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePictureSheet extends BottomSheetDialogFragment {

    public static final String TAG = "ProfilePictureSheet";

    private Button viewButton;
    private Button updateButton;
    private int userId;

    private MediaPicker mediaPicker;
    private ProfilePictureUpdater updater;

    private ActivityResultLauncher<Intent> pickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                mediaPicker.pickedImageAction(result);
                updater.sendUpdateRequest(mediaPicker);
            });


    public ProfilePictureSheet(AppCompatActivity activity, int userId){
        this.userId = userId;
        mediaPicker = new MediaPicker(activity, pickerLauncher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_profile_picture_option_sheet, container, false);

        viewButton = panel.findViewById(R.id.profilepic_view_button);
        updateButton = panel.findViewById(R.id.profilepic_edit_button);

        addOnClickListeners();
        return panel;
    }

    private void addOnClickListeners(){
        viewButton.setOnClickListener(view -> {
        });
        updateButton.setOnClickListener(view -> {
            if(updater == null) {
                updater = new ProfilePictureUpdater(userId, mediaPicker);
            }
            updater.update();
            dismiss();
        });
    }

}
