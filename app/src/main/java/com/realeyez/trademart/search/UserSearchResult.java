package com.realeyez.trademart.search;

import android.net.Uri;

public class UserSearchResult {

    private SearchResult searchResult;
    private Uri profilePictureUri;

    public UserSearchResult(SearchResult searchResult, Uri profileUri){
        this.searchResult = searchResult;
        profilePictureUri = profileUri;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public Uri getProfilePictureUri() {
        return profilePictureUri;
    }


}
