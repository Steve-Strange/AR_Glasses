package com.example.ar_glasses;

public class TutorialItem {
    private int imageResId;
    private String title;
    private String content;

    public TutorialItem(int imageResId, String title, String content) {
        this.imageResId = imageResId;
        this.title = title;
        this.content = content;
    }

    // Getters
    public int getImageResId() { return imageResId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
}