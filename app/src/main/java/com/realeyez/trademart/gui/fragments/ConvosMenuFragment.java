package com.realeyez.trademart.gui.fragments;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.messaging.ConvoPanel;
import com.realeyez.trademart.messaging.ChatType;
import com.realeyez.trademart.messaging.ConvoInfo;
import com.realeyez.trademart.request.Content;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.resource.ResourceRepository;
import com.realeyez.trademart.util.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

public class ConvosMenuFragment extends Fragment {

    private ArrayList<ConvoInfo> convoInfos;
    private LinearLayout listPanel;

    public ConvosMenuFragment(){
        super(R.layout.fragment_convo_menu);
        convoInfos = new ArrayList<>();
    }

    public ConvosMenuFragment(ArrayList<ConvoInfo> convoInfos){
        super(R.layout.fragment_convo_menu);
        this.convoInfos = convoInfos;
    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if(!convoInfos.isEmpty()){
                convoInfos.clear();
            }
            loadConvoInfos();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_convo_menu, container, false);
        
        listPanel = layout.findViewById(R.id.convo_list_panel);

        return layout;
    }


    private void loadConvoInfos(){
        Content content = new Content.ContentBuilder()
            .put("user_id", ResourceRepository.getResources().getCurrentUser().getId())
            .build();
        try {
            Response response = RequestUtil.sendPostRequest("/message/convos", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String errMsg = json.getString("message");
                getActivity().runOnUiThread(() -> {
                    Dialogs.showErrorDialog(errMsg, getContext());
                });
                return;
            }
            JSONArray convos = json.getJSONObject("data").getJSONArray("convos");
            for (int i = 0; i < convos.length(); i++) {
                JSONObject convo = convos.getJSONObject(i);
                JSONObject lastChat = convo.getJSONObject("last_chat");
                ChatType type = ChatType.parse(lastChat.getString("type"));
                ConvoInfo convoInfo = new ConvoInfo.Builder()
                    .setUsername(convo.getString("username"))
                    .setUserId(convo.getInt("user_id"))
                    .setConvoId(convo.getInt("convo_id"))
                    .setTimestamp(LocalDateTime.parse(lastChat.getString("time_sent")))
                    .setType(type)
                    .setLastMessage(type == ChatType.MESSAGE ?
                            lastChat.getString("message") : null)
                    .build();
                convoInfos.add(convoInfo);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(() -> {
                Dialogs.showErrorDialog("Unable to get conversations", getContext());
            });
            return;
        }

        getActivity().runOnUiThread(() -> {
            for (ConvoInfo info : convoInfos) {
                listPanel.addView(ConvoPanel.inflate(getLayoutInflater(), info));
            }
        });

    }

    
}
