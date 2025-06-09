package com.realeyez.trademart.gui.components.profile;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShowcaseRow {

    private LinearLayout basePanel;
    private LinearLayout rowPanel;

    private FrameLayout frame1;
    private FrameLayout frame2;

    private ImageView image1;
    private ImageView image2;

    private Context context;
    private ArrayList<ShowcaseRow> rows;

    public ShowcaseRow(Context context, LinearLayout basePanel, ArrayList<ShowcaseRow> rows){
        this.basePanel = basePanel;
        this.context = context;
        image1 = null;
        image2 = null;
        initComponents();
    }
    
    private void initComponents(){
         rowPanel = new LinearLayout(context);
         LinearLayout.LayoutParams row_params = new LinearLayout.LayoutParams(
                 LayoutParams.MATCH_PARENT,
                 150
                 );
         row_params.gravity = Gravity.START;
         rowPanel.setOrientation(LinearLayout.HORIZONTAL);
         rowPanel.setLayoutParams(row_params);

         frame1 = new FrameLayout(context);
         initFrame(frame1);
         frame2 = new FrameLayout(context);
         initFrame(frame2);
         rowPanel.addView(frame1);
         rowPanel.addView(frame2);
    }

    private void initFrame(FrameLayout frame){
         LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                 LayoutParams.MATCH_PARENT,
                 LayoutParams.WRAP_CONTENT,
                 0.5f
                 );
         frame.setLayoutParams(params);
    }

    public boolean AddImage(ImageView image){
        if(image1 != null && image2 != null)
            return false;
        if(image1 == null){
            image1 = image;
            frame1.addView(image1);
        } else if(image1 != null && image2 == null) {
            image2 = image;
            frame2.addView(image2);
        }
        return true;
    }

    public int getImageCount(){
        int count = 0;
        if(image1 != null) count++;
        if(image2 != null) count++;
        return count;
    }

    public LinearLayout getRowPanel() {
        return rowPanel;
    }

}
