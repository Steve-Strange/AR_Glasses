package com.example.ar_glasses;

import java.util.List;

public class PhotoGroup {
    private String date;
    private List<String> photoPaths;

    public PhotoGroup(String date, List<String> photoPaths) {
        this.date = date;
        this.photoPaths = photoPaths;
    }

    public String getDate() {
        return date;
    }

    public List<String> getPhotoPaths() {
        return photoPaths;
    }
}