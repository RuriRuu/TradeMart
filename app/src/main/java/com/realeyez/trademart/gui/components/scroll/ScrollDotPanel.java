package com.realeyez.trademart.gui.components.scroll;

import java.util.ArrayList;

import android.content.Context;
import android.widget.LinearLayout;

public class ScrollDotPanel {

    private Context context;
    private LinearLayout panel;
    private ArrayList<ScrollDot> dots;
    private int lastActive;

    public ScrollDotPanel(Context context, LinearLayout panel, int size){
        this.context = context;
        this.panel = panel;
        lastActive = 0;
        dots = new ArrayList<>(size);
        for(int i = 0; i < size; i++){
            addDot();
        }
        dots.get(0).setActive(true);
    }

    private void addDot(){
        ScrollDot dot = new ScrollDot(context);
        dots.add(dot);
        panel.addView(dot.getImageView());
    }

    public void setActive(int index){
        dots.get(lastActive).setActive(false);
        dots.get(index).setActive(true);
        lastActive = index;
    }


}
