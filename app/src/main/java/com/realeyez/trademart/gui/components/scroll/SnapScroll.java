package com.realeyez.trademart.gui.components.scroll;

import android.view.MotionEvent;
import android.widget.ScrollView;

public class SnapScroll {

    private ScrollView scrollView;
    private int lastY;
    private int curChild;

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
        if (scrollView.getScrollY() > lastY + (scrollView.getWidth() / 2)) {
            int y = ++curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(scrollView.getWidth(), y);
            lastY = y;
            return true;
        }
        // snap to next
        if (scrollView.getScrollY() < lastY - (scrollView.getWidth() / 2)) {
            int y = --curChild * scrollView.getWidth();
            scrollView.smoothScrollTo(scrollView.getWidth(), y);
            lastY = y;
            return true;
        }
        // snap back
        scrollView.smoothScrollTo(lastY, scrollView.getScrollY());
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
