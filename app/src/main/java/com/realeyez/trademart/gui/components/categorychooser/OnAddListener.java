package com.realeyez.trademart.gui.components.categorychooser;

import com.realeyez.trademart.service.FeedCategory;

@FunctionalInterface
public interface OnAddListener {

    public void onAdd(FeedCategory selected);
    
}
