package com.realeyez.trademart;

public class User {
    public int id;

    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public String toString(){
        return name + " " + email + " " + password;
    }

}
