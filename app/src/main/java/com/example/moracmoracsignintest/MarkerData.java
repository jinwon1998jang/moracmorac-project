package com.example.moracmoracsignintest;

import java.util.HashMap;

public class MarkerData {
    private String id;
    private String title;
    private String content;
    private HashMap<String, String> openingHours; // 요일별 영업 시간을 저장하는 HashMap
    private double latitude;
    private double longitude;

    public MarkerData() {
        // Default constructor required for Firebase
    }

    public MarkerData(String id, String title, String content, HashMap<String, String> openingHours, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.openingHours = openingHours;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<String, String> getOpeningHours() {
        return openingHours;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
