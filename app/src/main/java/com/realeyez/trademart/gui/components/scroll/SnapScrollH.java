package com.realeyez.trademart.gui.components.scroll;

import com.realeyez.trademart.gui.components.event.OnChangeChildListener;

import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class SnapScrollH {

    private HorizontalScrollView scrollView;
    private int lastX;
    private int curChild;
    private OnChangeChildListener onChangeChildListener;

    public SnapScrollH(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
        initComponents();
    }

    private void initComponents() {
        curChild = 0;
        onChangeChildListener = null;
        applyScrollEvent();
    }

    private boolean snap() {
        // snap to prev
        int lastChild = curChild;
        if (scrollView.getScrollX() > lastX + (scrollView.getWidth() / 2)) {
            int x = ++curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
            if(onChangeChildListener != null)
                onChangeChildListener.onChangeChild(lastChild, curChild);
            return true;
        }
        // snap to next
        if (scrollView.getScrollX() < lastX - (scrollView.getWidth() / 2)) {
            int x = --curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
            if(onChangeChildListener != null)
                onChangeChildListener.onChangeChild(lastChild, curChild);
            return true;
        }
        // snap back
        scrollView.smoothScrollTo(lastX, scrollView.getScrollY());
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

    public int getCurChild(){
        return curChild;
    }

    public void setOnChangeChildListener(OnChangeChildListener onChangeChildListener) {
        this.onChangeChildListener = onChangeChildListener;
    }

}
