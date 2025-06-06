package com.realeyez.trademart.user;

public class User {

    private int id;

    private String username;
    private String name;
    private String email;
    private String password;

    public User(UserBuilder builder){
        this.id = builder.id;
        this.username = builder.username;
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
    }

    public int getId() {
        return id;
    }

    public String getUsername(){
        return username;
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

    public static class UserBuilder {

        private int id;
        private String username;
        private String name;
        private String email;
        private String password;

        public UserBuilder(){
            id = 0;
            username = name = email = password = null;
        }

        public UserBuilder setId(int id){
            this.id = id;
            return this;
        }

        public UserBuilder setUsername(String username){
            this.username = username;
            return this;
        }

        public UserBuilder setName(String name){
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email){
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password){
            this.password = password;
            return this;
        }

        public User build(){
            return new User(this);
        }

    }

}
