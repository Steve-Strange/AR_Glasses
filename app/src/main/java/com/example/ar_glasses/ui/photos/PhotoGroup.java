package com.example.ar_glasses.ui.photos;

import java.util.List;

public class PhotoGroup {
    private String date;
    private List<MediaStoreImage> photos;

    public PhotoGroup(String date, List<MediaStoreImage> photos) {
        this.date = date;
        this.photos = photos;
    }

    public String getDate() {
        return date;
    }

    public List<MediaStoreImage> getPhotos() {
        return photos;
    }
}