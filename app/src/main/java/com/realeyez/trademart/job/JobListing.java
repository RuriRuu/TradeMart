package com.realeyez.trademart.job;

import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;


public class JobListing {

    private int id;
    private String title;
    private String description;
    private double amount;
    private int likes;
    private LocalDateTime datePosted;
    private int employerId;


    public JobListing(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.likes = builder.likes;
        this.description = builder.description;
        this.amount = builder.amount;
        this.datePosted = builder.datePosted;
        this.employerId = builder.employerId;
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public int getLikes() {
        return likes;
    }

    public int getEmployerId() {
        return employerId;
    }

    public JSONObject parseJson() throws JSONException {
        return new JSONObject()
            .put("job_id", id)
            .put("job_title", title)
            .put("job_description", description)
            .put("amount", amount)
            .put("date_posted", datePosted)
            .put("employer_id", employerId);
    }

    public static class Builder {

        private int id;
        private String title;
        private String description;
        private double amount;
        private int likes;
        private LocalDateTime datePosted;
        private int employerId;

        public Builder(){
            id = employerId = -1;
            amount = 0;
            likes = 0;
            description = "";
            title = null;
            datePosted = null;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setLikes(int likes) {
            this.likes = likes;
            return this;
        }

        public Builder setDatePosted(LocalDateTime datePosted) {
            this.datePosted = datePosted;
            return this;
        }

        public Builder setEmployerId(int employerId) {
            this.employerId = employerId;
            return this;
        }

        public JobListing build(){
            return new JobListing(this);
        }

        public JobListing fromJSON(JSONObject json) throws JSONException {
            return new Builder()
                .setId(json.getInt("service_id"))
                .setTitle(json.getString("service_title"))
                .setDescription(json.getString("service_description"))
                .setDatePosted(LocalDateTime.parse(json.getString("date_posted")))
                .setAmount(json.getDouble("amount"))
                .setEmployerId(json.getInt("employer_id"))
                .build();
        }


    }

}

