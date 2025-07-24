package com.realeyez.trademart.gui.components.categorychooser;

import java.util.ArrayList;

import com.realeyez.trademart.R;
import com.realeyez.trademart.service.FeedCategory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.annotation.Nullable;

public class CategoryAdd extends LinearLayout {

    private Spinner categorySpinner;
    private ImageButton addButton;

    private OnAddListener onAddListener;

    public CategoryAdd(Context context) {
        super(context);
        initComponents();
    }

    public CategoryAdd(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponents();
    }

    public CategoryAdd(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponents();
    }

    public CategoryAdd(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponents();
    }

    private void initComponents(){
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.HORIZONTAL);
        categorySpinner = new Spinner(getContext());
        categorySpinner.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        updateSpinnerItems(null);

        addButton = new ImageButton(getContext());
        addButton.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        addButton.setImageDrawable(getResources().getDrawable(R.drawable.baseline_add_24, null));

        addButton.setOnClickListener(view -> {
            if(onAddListener == null){
                return;
            }
            String category = (String) categorySpinner.getSelectedItem();
            FeedCategory selected = FeedCategory.valueOf(category);
            onAddListener.onAdd(selected);
        });

        addView(categorySpinner);
        addView(addButton);
    }

    public void updateSpinnerItems(ArrayList<FeedCategory> loadedCategories){
        ArrayList<String> items = new ArrayList<>();
        for(FeedCategory category : FeedCategory.values()){
            if(loadedCategories != null)
                if(loadedCategories.contains(category))
                    continue;
            if(category == FeedCategory.NONE){
                continue;
            }
            items.add(category.toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, items);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(0);
    }

    public void setOnAddListener(OnAddListener onAddListener){
        this.onAddListener = onAddListener;
    }

}
