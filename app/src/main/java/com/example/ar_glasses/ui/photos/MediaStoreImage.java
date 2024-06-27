package com.example.ar_glasses.ui.photos;

import android.net.Uri;

import androidx.recyclerview.widget.DiffUtil;

import java.util.Date;

public class MediaStoreImage {
    private final long id;
    private final String displayName;
    private final Date dateAdded;
    private final Uri contentUri;
    private final boolean isVideo;

    public MediaStoreImage(long id, String displayName, Date dateAdded, Uri contentUri, boolean isVideo) {
        this.id = id;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
        this.contentUri = contentUri;
        this.isVideo = isVideo;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Uri getContentUri() {
        return contentUri;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public static final DiffUtil.ItemCallback<MediaStoreImage> DiffCallback = new DiffUtil.ItemCallback<MediaStoreImage>() {
        @Override
        public boolean areItemsTheSame(MediaStoreImage oldItem, MediaStoreImage newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(MediaStoreImage oldItem, MediaStoreImage newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaStoreImage that = (MediaStoreImage) o;

        if (id != that.id) return false;
        if (!displayName.equals(that.displayName)) return false;
        if (!dateAdded.equals(that.dateAdded)) return false;
        return contentUri.equals(that.contentUri);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + displayName.hashCode();
        result = 31 * result + dateAdded.hashCode();
        result = 31 * result + contentUri.hashCode();
        return result;
    }
}
