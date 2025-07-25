package com.realeyez.trademart.gui.components.scroll;

import com.realeyez.trademart.gui.components.scroll.event.OnChangeChildListener;

import android.view.MotionEvent;
import android.widget.ScrollView;

public class SnapScroll {

    private ScrollView scrollView;
    private int lastY;
    private int curChild;
    private OnChangeChildListener onChangeChildListener;

    public SnapScroll(ScrollView scrollView) {
        this.scrollView = scrollView;
        initComponents();
    }

    private void initComponents() {
        curChild = 0;
        applyScrollEvent();
    }

    private boolean snap() {
        // snap to prev
        if (scrollView.getScrollY() > lastY + (scrollView.getHeight() / 2)) {
            int y = ++curChild * scrollView.getHeight();
            scrollView.smoothScrollTo(scrollView.getWidth(), y);
            lastY = y;
            if(onChangeChildListener != null)
                onChangeChildListener.onChangeChild(curChild-1, curChild);
            return true;
        }
        // snap to next
        if (scrollView.getScrollY() < lastY - (scrollView.getHeight() / 2)) {
            int y = --curChild * scrollView.getHeight();
            scrollView.smoothScrollTo(scrollView.getWidth(), y);
            lastY = y;
            if(onChangeChildListener != null)
                onChangeChildListener.onChangeChild(curChild+1, curChild);
            return true;
        }
        // snap back
        scrollView.smoothScrollTo(scrollView.getScrollX(), lastY);
        return false;
    }

    private void applyScrollEvent() {
        scrollView.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                snap();
                return true;
            }
            return false;
        });
    }

    public void refreshScroll(){
        int curY = curChild * scrollView.getHeight();
        if(lastY == curY){
            scrollView.scrollTo(scrollView.getWidth(), curY);
        }
    }

    public void resetChildren(){
        curChild = 0;
        lastY = 0;
    }

    public void setCurChild(int curChild){
        this.curChild = curChild;
    }

    public int getCurChild(){
        return curChild;
    }

    public void setOnChangeChildListener(OnChangeChildListener onChangeChildListener) {
        this.onChangeChildListener = onChangeChildListener;
    }

}
