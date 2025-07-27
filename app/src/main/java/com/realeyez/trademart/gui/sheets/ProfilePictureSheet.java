package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.ProfilePictureViewActivity;
import com.realeyez.trademart.R;
import com.realeyez.trademart.media.MediaPicker;
import com.realeyez.trademart.profile.ProfilePictureUpdater;
import com.realeyez.trademart.resource.ResourceRepository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class ProfilePictureSheet extends BottomSheetDialogFragment {

    public static final String TAG = "ProfilePictureSheet";

    private Button viewButton;
    private Button updateButton;
    private int userId;

    private MediaPicker mediaPicker;
    private ProfilePictureUpdater updater;

    private ActivityResultLauncher<Intent> pickerLauncher;

    public ProfilePictureSheet(int userId){
        this.userId = userId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_profile_picture_option_sheet, container, false);

        viewButton = panel.findViewById(R.id.profilepic_view_button);
        updateButton = panel.findViewById(R.id.profilepic_edit_button);

        addOnClickListeners();
        pickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    resultAction(result);
                });

        mediaPicker = new MediaPicker(requireActivity(), pickerLauncher);

        if(userId != ResourceRepository.getResources().getCurrentUser().getId()){
            panel.removeView(updateButton);
        }

        return panel;
    }

    private void resultAction(ActivityResult result){
        FragmentActivity activity = requireActivity();
        Bundle ret = new Bundle();

        mediaPicker.pickedImageAction(result);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                updater.sendUpdateRequest(mediaPicker);
                ret.putBoolean("success", true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                ret.putBoolean("success", false);
            } catch (IOException e) {
                e.printStackTrace();
                ret.putBoolean("success", false);
            }
            activity.getSupportFragmentManager().setFragmentResult("success", ret);
            activity.runOnUiThread(() -> dismiss());
        });
    }

    private void addOnClickListeners(){
        viewButton.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), ProfilePictureViewActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        updateButton.setOnClickListener(view -> {
            if(updater == null) {
                updater = new ProfilePictureUpdater(userId, mediaPicker);
            }
            updater.update();
        });
    }

}
