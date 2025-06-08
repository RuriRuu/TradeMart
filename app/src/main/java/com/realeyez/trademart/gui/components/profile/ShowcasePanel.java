package com.realeyez.trademart.gui.components.profile;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ShowcasePanel {

    private Context context;
    private LinearLayout basePanel;

    private ArrayList<ShowcaseRow> rows;

    public ShowcasePanel(Context context, LinearLayout basePanel) {
        this.context = context;
        this.basePanel = basePanel;
        this.rows = new ArrayList<>();
    }

    public void addImage(Uri imageUri) {
        ImageView image = new ImageView(context);
        LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, 
                LayoutParams.MATCH_PARENT, 
                0.5f);
        image_params.gravity = Gravity.START;
        image.setLayoutParams(image_params);
        image.setImageURI(imageUri);
        ShowcaseRow row = null;
        if(rows.size() == 0){
            row = addRow();
            rows.add(row);
        } else {
            row = rows.get(rows.size()-1);
            if(row.getImageCount() >= 2){
                row = addRow();
                rows.add(row);
            }
        }
        row.AddImage(image);
    }

    private ShowcaseRow addRow() {
        ShowcaseRow row = new ShowcaseRow(context, basePanel, rows);
        basePanel.addView(row.getRowPanel());
        rows.add(row);
        return row;
    }

}
