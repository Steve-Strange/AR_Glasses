package com.example.ar_glasses.ui.ai;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_glasses.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private List<ChatMessage> messages;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String topic = getIntent().getStringExtra("TOPIC");
        setTitle(topic);

        initRecyclerView();
        loadMessages();

        binding.sendButton.setOnClickListener(v -> sendMessage());
    }

    private void initRecyclerView() {
        messages = new ArrayList<>();
        adapter = new ChatMessageAdapter(messages);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(adapter);
    }

    private void loadMessages() {
        // 这里应该从数据库加载实际的消息
        // 这只是一个示例
        messages.add(new ChatMessage("Hello!", false));
        messages.add(new ChatMessage("Hi there! How can I help you?", true));
        adapter.notifyDataSetChanged();
    }

    private void sendMessage() {
        String messageText = binding.editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            messages.add(new ChatMessage(messageText, false));
            adapter.notifyItemInserted(messages.size() - 1);
            binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
            binding.editTextMessage.setText("");

            // 这里应该实现发送消息到服务器的逻辑
            // 然后接收并显示AI的回复
        }
    }
}