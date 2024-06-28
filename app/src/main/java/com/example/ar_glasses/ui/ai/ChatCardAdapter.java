package com.example.ar_glasses.ui.ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.R;

import java.util.List;

public class ChatCardAdapter extends RecyclerView.Adapter<ChatCardAdapter.ChatCardViewHolder> {
    private List<ChatCard> ChatCards;
    private OnItemClickListener listener;

    public ChatCardAdapter(List<ChatCard> ChatCards) {
        this.ChatCards = ChatCards;
    }

    @NonNull
    @Override
    public ChatCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_card, parent, false);
        return new ChatCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCardViewHolder holder, int position) {
        ChatCard ChatCard = ChatCards.get(position);
        holder.textViewTopic.setText(ChatCard.getTopic());
        holder.textViewTime.setText(ChatCard.getFormattedTime());
    }


    @Override
    public int getItemCount() {
        return ChatCards.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ChatCardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTopic;
        TextView textViewTime;

        public ChatCardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTopic = itemView.findViewById(R.id.text_view_topic);
            textViewTime = itemView.findViewById(R.id.text_view_time);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(ChatCards.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ChatCard ChatCard);
    }
}