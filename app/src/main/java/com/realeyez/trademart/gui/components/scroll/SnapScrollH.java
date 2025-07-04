package com.realeyez.trademart.gui.components.scroll;

import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class SnapScrollH {

    private HorizontalScrollView scrollView;
    private int lastX;
    private int curChild;

    public SnapScrollH(HorizontalScrollView scrollView) {
        this.scrollView = scrollView;
        initComponents();
    }

    private void initComponents() {
        curChild = 0;
        applyScrollEvent();
    }

    private boolean snap() {
        // snap to prev
        if (scrollView.getScrollX() > lastX + (scrollView.getWidth() / 2)) {
            int x = ++curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
            return true;
        }
        // snap to next
        if (scrollView.getScrollX() < lastX - (scrollView.getWidth() / 2)) {
            int x = --curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(x, scrollView.getHeight());
            lastX = x;
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

}
