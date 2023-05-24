package com.example.moracmoracsignintest;

public class MarkerData {
    private String name;
    private String content;
    private double latitude;
    private double longitude;

    public MarkerData() {
        // Default constructor required for Firebase
    }

    public MarkerData(String name, String content, double latitude, double longitude) {
        this.name = name;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
