package com.example.ar_glasses.ui.photos;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ar_glasses.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_PHOTO = 1;

    private List<Object> items = new ArrayList<>();

    public void setPhotoGroups(List<PhotoGroup> photoGroups) {
        items.clear();
        for (PhotoGroup group : photoGroups) {
            items.add(group.getDate()); // 添加日期作为头部
            items.addAll(group.getPhotos()); // 添加对应的图片列表
        }
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return VIEW_TYPE_DATE;
        } else {
            return VIEW_TYPE_PHOTO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_date_header, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_photo, parent, false);
            return new PhotoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_DATE) {
            ((DateViewHolder) holder).bind((String) items.get(position));
        } else {
            ((PhotoViewHolder) holder).bind((MediaStoreImage) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public boolean isHeader(int position) {
        return getItemViewType(position) == VIEW_TYPE_DATE;
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        DateViewHolder(View itemView) {
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

        PhotoViewHolder(View itemView) {
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