package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.dialogs.LoadingDialog;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import androidx.fragment.app.FragmentActivity;

public class CreatePaymentSheet extends BottomSheetDialogFragment {

    public static final String TAG = "CreatePaymentSheet";

    private EditText amountField;
    private Spinner typeSpinner;
    private Spinner selectionSpinner;
    private Button payButton;

    private ArrayList<String> serviceTitles;
    private ArrayList<Integer> serviceIds;
    private ArrayList<String> jobTitles;
    private ArrayList<Integer> jobIds;

    private int mateId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        LinearLayout panel =  (LinearLayout) inflater.inflate(R.layout.layout_create_payment_sheet, container, false);

        amountField = panel.findViewById(R.id.paysheet_amount);
        typeSpinner = panel.findViewById(R.id.paysheet_type_dropdown);
        selectionSpinner = panel.findViewById(R.id.paysheet_selection_dropdown);
        payButton = panel.findViewById(R.id.paysheet_pay_button);

        String[] types = getResources().getStringArray(R.array.payment_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(0);

        loadData();

        return panel;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        addOnClickListeners();
    }

    private void loadData(){
        Bundle args = getArguments();
        mateId = args.getInt("mate_id", -1);
        serviceTitles = args.getStringArrayList("service_titles");
        serviceIds = args.getIntegerArrayList("service_ids");
        jobTitles = args.getStringArrayList("job_titles");
        jobIds = args.getIntegerArrayList("job_ids");

        if(serviceTitles.isEmpty() && !jobTitles.isEmpty()){
            typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, List.of("Job Listing")));
            selectionSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, jobTitles));
        } else if(!serviceTitles.isEmpty() && jobTitles.isEmpty()){
            typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, List.of("Service")));
            selectionSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, serviceTitles));
        } else if(serviceTitles.isEmpty() && jobTitles.isEmpty()){
            Dialogs.showMessageDialog("There is nothing to pay for!", requireContext());
            dismiss();
        } else if(!serviceTitles.isEmpty() && !jobTitles.isEmpty()){
            selectionSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, jobTitles));
        }

    }

    private JSONObject sendPayment(){
        if(typeSpinner.getSelectedItem().toString().equals("Service")){
            if(serviceIds.isEmpty()){
                getActivity().runOnUiThread(() -> {
                    Dialogs.showErrorDialog("This user does not have any services", getActivity());
                });
                return null;
            }
            return sendServicePayment();
        } else if(typeSpinner.getSelectedItem().toString().equals("Job Listing")){
            if(jobIds.isEmpty()){
                getActivity().runOnUiThread(() -> {
                    Dialogs.showErrorDialog("This user does not have any job listings", getActivity());
                });
                return null;
            }
            return sendJobPayment();
        }
        return null;
    }

    private JSONObject sendServicePayment(){
        int serviceId = serviceIds.get(selectionSpinner.getSelectedItemPosition());

        Content content = new Content.ContentBuilder()
            .put("amount", amountField.getText().toString())
            .put("sender_id", ResourceRepository.getResources().getCurrentUser().getId())
            .put("receiver_id", mateId)
            .put("service_id", serviceId)
            .build();
        try {
            Response response = RequestUtil.sendPostRequest("/payment/create/service", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                getActivity().runOnUiThread(() -> Dialogs.showErrorDialog(rMessage, getContext()));
                return null;
            }
            return json.getJSONObject("entity");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(() -> Dialogs.showErrorDialog("unable to create a payment, Try again later!", getContext()));
            return null;
        } 
    }

    private JSONObject sendJobPayment(){
        int jobId = jobIds.get(selectionSpinner.getSelectedItemPosition());
        Logger.log("jobId: " + jobId, LogLevel.INFO);

        Content content = new Content.ContentBuilder()
            .put("amount", amountField.getText().toString())
            .put("sender_id", ResourceRepository.getResources().getCurrentUser().getId())
            .put("receiver_id", mateId)
            .put("job_id", jobId)
            .build();

        try {
            Response response = RequestUtil.sendPostRequest("/payment/create/job", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String rMessage = json.getString("message");
                getActivity().runOnUiThread(() -> Dialogs.showErrorDialog(rMessage, getContext()));
                return null;
            }
            return json.getJSONObject("entity");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(() -> Dialogs.showErrorDialog("unable to create a payment, Try again later!", getContext()));
            return null;
        } 
    }

    private void addOnClickListeners(){
        FragmentActivity activity = requireActivity();
        payButton.setOnClickListener(view -> {
            LoadingDialog loader = new LoadingDialog(requireActivity());
            loader.show();
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(() -> {
                Bundle result = new Bundle();
                JSONObject json = sendPayment();
                loader.close();
                if(json == null){
                    getActivity().runOnUiThread(() -> dismiss());
                    return;
                }
                result.putString("json_data", json.toString());
                activity.getSupportFragmentManager().setFragmentResult("pay_result", result);
            });
            dismiss();
        });
        typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                // parent.clear
                if(position == 0){
                    if(!serviceTitles.isEmpty()){
                        selectionSpinner.setAdapter(new ArrayAdapter<>(
                                    getContext(), 
                                    android.R.layout.simple_spinner_item, 
                                    serviceTitles));
                    }
                }
                if(position == 1){
                    if(!jobTitles.isEmpty()){
                        selectionSpinner.setAdapter(new ArrayAdapter<>(
                                    getContext(), 
                                    android.R.layout.simple_spinner_item, 
                                    jobTitles));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
    }
    
}
