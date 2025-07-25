package com.realeyez.trademart.gui.components.search.event;

import com.realeyez.trademart.search.SearchResult;

@FunctionalInterface
public interface OnSearchItemClickedListener {

    public void onSearchItemClicked(SearchResult result);
}
