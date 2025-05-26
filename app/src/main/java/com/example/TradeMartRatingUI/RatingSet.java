package com.example.TradeMartRatingUI;

public class RatingSet {
    private static int ratingCount = 0;

    private final int ratingID;
    private final float communicationRating;
    private final float qualityRating;
    private final float speedRating;
    private final float valueRating;
    private final float overall;

    public RatingSet(float communicationRating, float qualityRating, float speedRating, float valueRating) {
        this.ratingID = ++RatingSet.ratingCount;
        this.communicationRating = communicationRating;
        this.qualityRating = qualityRating;
        this.speedRating = speedRating;
        this.valueRating = valueRating;
        this.overall = (float) ((communicationRating + qualityRating + speedRating + valueRating) / 4.0);
    }

    public int getRatingID() {
        return ratingID;
    }

    public float getOverall() {
        return overall;
    }

    public float getCommunicationRating() {
        return communicationRating;
    }

    public float getQualityRating() {
        return qualityRating;
    }

    public float getSpeedRating() {
        return speedRating;
    }

    public float getValueRating() {
        return valueRating;
    }
}