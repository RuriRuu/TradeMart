package com.realeyez.trademart.gui.components.categorychooser;

import java.util.ArrayList;

import com.realeyez.trademart.R;
import com.realeyez.trademart.service.FeedCategory;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class CategoryChooser extends ConstraintLayout {

    private LinearLayout inputPanel;
    private ArrayList<CategoryItem> categoryItems;
    private ArrayList<FeedCategory> loadedCategories;
    private CategoryAdd adder;

    public CategoryChooser(@NonNull Context context) {
        super(context);
    }

    public CategoryChooser(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategoryChooser(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onFinishInflate(){
        super.onFinishInflate();
        initComponents();
    }

    private void initComponents(){
        categoryItems = new ArrayList<>();
        loadedCategories = new ArrayList<>();
        inputPanel = findViewById(R.id.categorychooser_categories_input_panel);
        adder = new CategoryAdd(getContext());
        adder.setId(generateViewId());
        addView(adder);
        ConstraintSet constraint = new ConstraintSet();
        constraint.clone(this);
        constraint.connect(adder.getId(), ConstraintSet.TOP, inputPanel.getId(), ConstraintSet.BOTTOM);
        constraint.connect(adder.getId(), ConstraintSet.BOTTOM, getId(), ConstraintSet.BOTTOM);
        constraint.connect(adder.getId(), ConstraintSet.START, getId(), ConstraintSet.START);
        constraint.connect(adder.getId(), ConstraintSet.END, getId(), ConstraintSet.END);
        constraint.applyTo(this);
        addListeners();
    }

    private void addCategoryItem(FeedCategory selected){
        CategoryItem item = new CategoryItem(getContext(), selected);
        item.setOnRemove(view -> onItemRemoved(item));
        categoryItems.add(item);
        // inputPanel.removeView(adder);
        inputPanel.addView(item);
        // inputPanel.addView(adder);
    }

    private void onItemRemoved(CategoryItem item){
        FeedCategory category = item.getCategory();
        for (int i = 0; i < inputPanel.getChildCount(); i++) {
            if(inputPanel.getChildAt(i).equals(item)){
                loadedCategories.remove(category);
                inputPanel.removeView(item);
                categoryItems.remove(i);
                adder.updateSpinnerItems(loadedCategories);
            }
        }
    }

    public static CategoryChooser inflate(Activity activity,  LayoutParams params){
        CategoryChooser view = (CategoryChooser) activity.getLayoutInflater()
            .inflate(R.layout.layout_category_chooser, null);
        view.setLayoutParams(params);
        return view;
    }

    public ArrayList<FeedCategory> getChosenCategories(){
        return loadedCategories;
    }

    public ArrayList<String> getChosenCategoriesString(){
        ArrayList<String> categories = new ArrayList<>();
        for (FeedCategory category : loadedCategories) {
            categories.add(category.toString());
        }
        return categories;
    }


    private void onAddAction(FeedCategory selected){
        addCategoryItem(selected);
        loadedCategories.add(selected);
        adder.updateSpinnerItems(loadedCategories);
    }

    private void addListeners(){
        adder.setOnAddListener(selected -> {
            onAddAction(selected);
        });
    }

}
