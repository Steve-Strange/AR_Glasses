package com.example.ar_glasses.ui.photos;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ar_glasses.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private List<Object> items = new ArrayList<>();

    private List<MediaItem> mediaItems = new ArrayList<>();

    public void setMediaItems(List<MediaItem> mediaItems) { // Changed method signature
        this.mediaItems = mediaItems;notifyDataSetChanged();
    }

    public void setPhotoGroups(List<PhotoGroup> photoGroups) {
        items.clear();
        for (PhotoGroup group : photoGroups) {
            items.add(group.getDate()); // 添加日期作为头部
            items.addAll(group.getPhotos()); // 添加对应的图片列表
        }
        notifyDataSetChanged();
    }

    public boolean isHeader(int position) {
        return getItemViewType(position) == TYPE_HEADER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
            return new PhotoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind((String) items.get(position));
        } else if (holder instanceof PhotoViewHolder) {
            ((PhotoViewHolder) holder).bind((MediaStoreImage) items.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_PHOTO;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
        }

        void bind(String date) {
            dateText.setText(date);
        }
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        SquareImageView photoImage;
        ImageView videoIcon;
        MediaStoreImage image;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.photoImage);
            videoIcon = itemView.findViewById(R.id.videoIcon);
            itemView.setOnClickListener(v -> openImage());
        }

        void bind(MediaStoreImage image) {
            this.image = image;
            Glide.with(photoImage.getContext())
                    .load(image.getContentUri())
                    .into(photoImage);

            if (image.isVideo()) {
                videoIcon.setVisibility(View.VISIBLE);
            } else {
                videoIcon.setVisibility(View.GONE);
            }
        }

        void openImage() {
            Intent intent = new Intent(Intent.ACTION_VIEW, image.getContentUri());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            itemView.getContext().startActivity(intent);
        }
    }
}
