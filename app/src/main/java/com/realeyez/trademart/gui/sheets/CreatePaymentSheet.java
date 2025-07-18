package com.realeyez.trademart.gui.sheets;

import com.realeyez.trademart.R;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.fragment.app.FragmentActivity;

public class CreatePaymentSheet extends BottomSheetDialogFragment {

    public static final String TAG = "CreatePaymentSheet";

    private EditText amountField;
    private Spinner typeSpinner;
    private Spinner selectionSpinner;
    private Button payButton;

    private ArrayList<String> serviceTitles;
    private ArrayList<Integer> serviceIds;
    private String[] jobTitles;
    private int[] jobIds;

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

        if(!serviceTitles.isEmpty()){
            selectionSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, serviceTitles));
        }
    }

    private JSONObject sendPayment(){
        if(typeSpinner.getSelectedItemPosition() == 0){
            return sendServicePayment();
        } else if(typeSpinner.getSelectedItemPosition() == 1){
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
        return null;
    }

    private void addOnClickListeners(){
        FragmentActivity activity = requireActivity();
        payButton.setOnClickListener(view -> {
            ExecutorService exec = Executors.newSingleThreadExecutor();
            exec.execute(() -> {
                Bundle result = new Bundle();
                JSONObject json = sendPayment();
                if(json == null){
                    getActivity().runOnUiThread(() -> dismiss());
                    return;
                }
                result.putString("json_data", json.toString());
                activity.getSupportFragmentManager().setFragmentResult("pay_result", result);
            });
            dismiss();
        });
    }
    
}
