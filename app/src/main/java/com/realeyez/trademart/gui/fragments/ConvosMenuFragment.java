package com.realeyez.trademart.gui.fragments;

import java.util.ArrayList;

import com.realeyez.trademart.R;
import com.realeyez.trademart.gui.components.messaging.ConvoPanel;
import com.realeyez.trademart.messaging.ConvoInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class ConvosMenuFragment extends Fragment {

    private ArrayList<ConvoInfo> convoInfos;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout layout = (FrameLayout) inflater.inflate(R.layout.fragment_convo_menu, container, false);
        
        LinearLayout listPanel = layout.findViewById(R.id.convo_list_panel);

        for (ConvoInfo info : convoInfos) {
            listPanel.addView(ConvoPanel.inflate(inflater, info));
        }

        return layout;
    }


    
}
