package com.realeyez.trademart.gui.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.realeyez.trademart.util.CacheFile;
import com.realeyez.trademart.util.Dialogs;
import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

public class ConvosMenuFragment extends Fragment {

    private Context appContext;

    private ArrayList<ConvoInfo> convoInfos;
    private LinearLayout listPanel;
    private Activity activity;

    public ConvosMenuFragment(){
        super(R.layout.fragment_convo_menu);
        convoInfos = new ArrayList<>();
    }

    public ConvosMenuFragment(ArrayList<ConvoInfo> convoInfos){
        super(R.layout.fragment_convo_menu);
        this.convoInfos = convoInfos;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        appContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activity = getActivity();
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
            Response response = RequestUtil.sendPostRequest("/message/convos/user", content);
            JSONObject json = response.getContentJson();
            if(json.getString("status").equals("failed")){
                String errMsg = json.getString("message");
                getActivity().runOnUiThread(() -> {
                    Dialogs.showErrorDialog(errMsg, requireContext());
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
                Dialogs.showErrorDialog("Unable to get conversations", requireContext());
            });
            return;
        }

        convoInfos.sort(Comparator.comparing((info) -> ((ConvoInfo)info).getTimestamp()).reversed());
        for (ConvoInfo info : convoInfos) {
            Uri mateUri = cacheProfilePicture(info.getUserId());
            Uri userUri = cacheProfilePicture(ResourceRepository.getResources().getCurrentUser().getId());
            activity.runOnUiThread(() -> {
                listPanel.addView(ConvoPanel.inflate(activity.getLayoutInflater(), info, mateUri, userUri));
            });
        }

    }

    private Uri cacheProfilePicture(int mateId){
        Uri uri = null;
        String path = new StringBuilder()
            .append("/user/")
            .append(mateId)
            .append("/avatar")
            .toString();
        try {
            Response response = RequestUtil.sendGetRequest(path);
            String filename = response.getContentDispositionField("filename");
            CacheFile cache = CacheFile.cache(appContext.getCacheDir(), filename, response.getContentBytes());
            File file = cache.getFile();
            uri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            Logger.log("User has no profile picture :(", LogLevel.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    
}
