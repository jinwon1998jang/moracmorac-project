package com.example.moracmoracsignintest;

public class MarkerData {
    private String id;
    private String title; // 변경된 필드 이름
    private String content;
    private String hours; // New field for hours
    private double latitude;
    private double longitude;

    public MarkerData() {
        // Default constructor required for Firebase
    }

    public MarkerData(String id, String title, String content, String hours, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hours = hours;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getHours() {
        return hours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
