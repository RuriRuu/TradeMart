package com.realeyez.trademart.gui.components.scroll;

import com.realeyez.trademart.util.DimensionsUtil;
import com.realeyez.trademart.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ScrollDot {

    private Context context;
    private ImageView image;

    public ScrollDot(Context context){
        this.context = context;
        initComponents();
    }

    private void initComponents(){
        image = new ImageView(context);
        int density = DimensionsUtil.getScreenDensity(context);
        LayoutParams params = new LayoutParams(density * 10, density * 10);
        params.setMarginStart(density * 2);
        params.setMarginEnd(density * 2);
        image.setLayoutParams(params);
        image.setScaleType(ScaleType.FIT_CENTER);
        setDotImage(false);
    }

    private void setDotImage(boolean isActive){
        int drawableId = isActive ? R.drawable.dot : R.drawable.dot_dim;
        image.setImageDrawable(context.getResources().getDrawable(drawableId, null));
    }

    public void setActive(boolean b){
        setDotImage(b);
    }

    public ImageView getImageView() {
        return image;
    }

}
