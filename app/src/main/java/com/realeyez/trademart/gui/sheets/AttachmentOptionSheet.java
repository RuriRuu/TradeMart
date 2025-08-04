package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.R;
import com.realeyez.trademart.media.MediaPicker;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
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
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class AttachmentOptionSheet extends BottomSheetDialogFragment {

    public static final String TAG = "AttachmentOptionSheet";

    private Button mediaButton;
    private Button payButton;

    private int mateId;
    private int convoId;

    private MediaPicker picker;

    private ActivityResultLauncher<Intent> pickerLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_attachment_option_sheet, container, false);

        mediaButton = panel.findViewById(R.id.attachment_media_button);
        payButton = panel.findViewById(R.id.attachment_pay_button);

        loadData();
        return panel;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        pickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Logger.logi("picked media");
                    resultAction(result);
                });

        addOnClickListeners();
    }

    private void loadData(){
        Bundle args = getArguments();
        mateId = args.getInt("mate_id", -1);
        convoId = args.getInt("convo_id", -1);
    }

    private void resultAction(ActivityResult result){
        FragmentActivity activity = requireActivity();
        Bundle ret = new Bundle();

        picker.pickedImageAction(result);

        ret.putString("filename", picker.getFilename());
        ret.putByteArray("media_data", picker.readBytes());

        Logger.logi("result set");
        activity.getSupportFragmentManager().setFragmentResult("media_result", ret);
        activity.runOnUiThread(() -> dismiss());
    }

    private void addOnClickListeners(){
        mediaButton.setOnClickListener(view -> {
            Logger.logi("clicked media button");
            picker = new MediaPicker(requireActivity(), pickerLauncher);
            picker.show(new String[]{"image/*"});
        });
        payButton.setOnClickListener(view -> {
            CreatePaymentSheet bottomSheet = new CreatePaymentSheet();
            Bundle args = new Bundle();
            args.putInt("mate_id", mateId);
            args.putInt("convo_id", convoId);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                fetchServices(args);
                fetchJobs(args);
                requireActivity().runOnUiThread(() -> {
                    bottomSheet.setArguments(args);
                    bottomSheet.show(requireActivity().getSupportFragmentManager(), CreatePaymentSheet.TAG);
                });
                requireActivity().runOnUiThread(() -> dismiss());
            });
        });
    }

    private boolean fetchJobs(Bundle bundle){
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        bundle.putStringArrayList("job_titles", titles);
        bundle.putIntegerArrayList("job_ids", ids);
        String path = new StringBuilder()
            .append("/jobs/employer/")
            .append(ResourceRepository.getResources().getCurrentUser().getId())
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                getActivity().runOnUiThread(() -> Dialogs.showErrorDialog(rMessage, getContext()));
                return false;
            }
            if(!json.getJSONObject("data").has("jobs")){
                return false;
            }
            JSONArray servArr = json.getJSONObject("data").getJSONArray("jobs");
            for (int i = 0; i < servArr.length(); i++) {
                JSONObject jobJson = servArr.getJSONObject(i);
                ids.add(jobJson.getInt("job_id"));
                titles.add(jobJson.getString("job_title"));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog("You currently don't have any Job Listings", getActivity());
                dismiss();
            });
            return false;
        }
        return true;
    }

    private boolean fetchServices(Bundle bundle){
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        bundle.putStringArrayList("service_titles", titles);
        bundle.putIntegerArrayList("service_ids", ids);
        String path = new StringBuilder()
            .append("/service/list/")
            .append(mateId)
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                getActivity().runOnUiThread(() -> Dialogs.showErrorDialog(rMessage, getContext()));
                return false;
            }
            if(!json.getJSONObject("data").has("services")){
                return false;
            }
            JSONArray servArr = json.getJSONObject("data").getJSONArray("services");
            for (int i = 0; i < servArr.length(); i++) {
                JSONObject serviceJson = servArr.getJSONObject(i);
                ids.add(serviceJson.getInt("service_id"));
                titles.add(serviceJson.getString("service_title"));
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog("Unable to find services, try again", getActivity());
                dismiss();
            });
            return false;
        }
        return true;
    }
    
}
