package com.realeyez.trademart.resource;

import com.realeyez.trademart.user.User;

public class ResourceRepository {

    private static ResourceRepository instance;

    private User currentUser;

    public static ResourceRepository getResources(){
        if(instance == null){
            instance = new ResourceRepository();
        }
        return instance;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
