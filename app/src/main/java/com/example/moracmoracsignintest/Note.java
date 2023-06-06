package com.example.moracmoracsignintest;

import com.google.firebase.Timestamp;

public class Note {
    String id; // 이메일을 문서 ID로 저장할 추가 필드
    String title;
    String content;
    Timestamp timestamp;

    public Note() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}