package com.realeyez.trademart.gui.components.categorychooser;

import com.realeyez.trademart.R;
import com.realeyez.trademart.service.FeedCategory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class CategoryItem extends LinearLayout {

    private TextView categoryLabel;
    private ImageButton removeButton;
    private FeedCategory category;

    public CategoryItem(Context context, FeedCategory category) {
        super(context);
        this.category = category;
        initComponents();
    }

    public CategoryItem(Context context) {
        super(context);
        initComponents();
    }

    public FeedCategory getCategory() {
        return category;
    }

    public CategoryItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    public CategoryItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents();
    }

    public CategoryItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponents();
    }

    private void initComponents(){
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        categoryLabel = new TextView(getContext());
        categoryLabel.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        categoryLabel.setText(category != null ? category.toString() : "CATEGORY");
        categoryLabel.setGravity(Gravity.CENTER_VERTICAL);
        categoryLabel.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

        removeButton = new ImageButton(getContext());
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        removeButton.setImageDrawable(getResources().getDrawable(R.drawable.close_icon, null));

        addView(categoryLabel);
        addView(removeButton);
    }

    public void setOnRemove(OnClickListener onClick){
        removeButton.setOnClickListener(onClick);
    }

}
