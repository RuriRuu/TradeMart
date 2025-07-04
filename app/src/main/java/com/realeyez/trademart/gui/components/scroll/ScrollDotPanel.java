package com.realeyez.trademart.gui.components.scroll;

import java.util.ArrayList;

import android.content.Context;
import android.widget.LinearLayout;

public class ScrollDotPanel {

    private Context context;
    private LinearLayout panel;
    private ArrayList<ScrollDot> dots;

    public ScrollDotPanel(Context context, LinearLayout panel, int size){
        this.context = context;
        this.panel = panel;
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
        for (int i = 0; i < dots.size(); i++) {
            if(i == index)
                dots.get(i).setActive(true);
            else
                dots.get(i).setActive(false);
        }
    }


}
