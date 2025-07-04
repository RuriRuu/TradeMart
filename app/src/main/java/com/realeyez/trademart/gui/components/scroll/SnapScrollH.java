package com.realeyez.trademart.gui.components.scroll;

import com.realeyez.trademart.gui.components.event.OnChangeChildListener;

import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class SnapScrollH {

    private HorizontalScrollView scrollView;
    private int lastX;
    private int curChild;
    private OnChangeChildListener onCangeChildListener;

    public SnapScrollH(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
        initComponents();
    }

    private void initComponents() {
        curChild = 0;
        onCangeChildListener = null;
        applyScrollEvent();
    }

    private boolean snap() {
        // snap to prev
        if (scrollView.getScrollX() > lastX + (scrollView.getWidth() / 2)) {
            int x = ++curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
            if(onCangeChildListener != null)
                onCangeChildListener.onChangeChild(curChild);
            return true;
        }
        // snap to next
        if (scrollView.getScrollX() < lastX - (scrollView.getWidth() / 2)) {
            int x = --curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
            if(onCangeChildListener != null)
                onCangeChildListener.onChangeChild(curChild);
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

    public void setOnCangeChildListener(OnChangeChildListener onCangeChildListener) {
        this.onCangeChildListener = onCangeChildListener;
    }

}
