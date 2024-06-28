package com.example.ar_glasses.ui.ai;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.R;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.MessageViewHolder> {

    private List<com.example.ar_glasses.ui.ai.ChatMessage> messages;

    public ChatMessageAdapter(List<com.example.ar_glasses.ui.ai.ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        com.example.ar_glasses.ui.ai.ChatMessage message = messages.get(position);
        holder.messageText.setText(message.getContent());

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.messageText.getLayoutParams();

        if (message.isAi()) {
            holder.messageText.setBackgroundResource(R.drawable.ai_message_background);
            params.gravity = Gravity.START;
        } else {
            holder.messageText.setBackgroundResource(R.drawable.user_message_background);
            params.gravity = Gravity.END;
        }

        holder.messageText.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
