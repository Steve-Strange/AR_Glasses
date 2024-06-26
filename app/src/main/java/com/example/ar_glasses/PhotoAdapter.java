package com.example.ar_glasses;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_PHOTO = 1;

    private List<Object> items = new ArrayList<>();

    public void setPhotoGroups(List<PhotoGroup> photoGroups) {
        items.clear();
        for (PhotoGroup group : photoGroups) {
            items.add(group.getDate());
            items.addAll(group.getPhotoPaths());
        }
        Log.d("PhotoAdapter", "Total items: " + items.size());
        notifyDataSetChanged();
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
            ((PhotoViewHolder) holder).bind((String) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_PHOTO;
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
        ImageView photoImage;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.photoImage);
        }

        void bind(String photoPath) {
            Glide.with(photoImage.getContext())
                    .load(photoPath)
                    .into(photoImage);
        }
    }
}