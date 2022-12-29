package com.mp.hw2;

public class HighModel {
    private int score;
    private double latitude;
    private double longitude;
    public HighModel(int score, double latitude, double longitude){
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getScore() {
        return score;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
