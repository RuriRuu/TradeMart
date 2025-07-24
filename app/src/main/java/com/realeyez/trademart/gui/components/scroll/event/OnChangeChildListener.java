package com.realeyez.trademart.gui.components.scroll.event;

@FunctionalInterface
public interface OnChangeChildListener {

    public void onChangeChild(int lastChild, int curChild);
    
}
