package com.realeyez.trademart.gui.components.job.event;

import com.realeyez.trademart.job.JobItem;

@FunctionalInterface
public interface OnJobItemClickedListener {

    public void onJobItemClicked(JobItem jobItem);
    
}
