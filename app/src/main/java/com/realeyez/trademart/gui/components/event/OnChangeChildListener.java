package com.realeyez.trademart.gui.components.event;

@FunctionalInterface
public interface OnChangeChildListener {

    public void onChangeChild(int lastChild, int curChild);
    
}
