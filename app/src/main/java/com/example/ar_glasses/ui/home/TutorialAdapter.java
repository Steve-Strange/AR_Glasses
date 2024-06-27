package com.example.ar_glasses.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.R;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.TutorialViewHolder> {

    private List<TutorialItem> tutorials;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TutorialItem item);
    }

    public TutorialAdapter(List<TutorialItem> tutorials, OnItemClickListener listener) {
        this.tutorials = tutorials;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tutorial, parent, false);
        return new TutorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorialViewHolder holder, int position) {
        holder.bind(tutorials.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return tutorials.size();
    }

    static class TutorialViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        TutorialViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tutorial_image);
            titleView = itemView.findViewById(R.id.tutorial_title);
        }

        void bind(final TutorialItem item, final OnItemClickListener listener) {
            imageView.setImageResource(item.getImageResId());
            titleView.setText(item.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}