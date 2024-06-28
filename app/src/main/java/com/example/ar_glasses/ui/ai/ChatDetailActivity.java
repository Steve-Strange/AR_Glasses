package com.example.ar_glasses.ui.ai;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_glasses.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private List<ChatMessage> messages;
    private ChatMessageAdapter adapter;
    private Handler mainHandler;

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

        mainHandler = new Handler(Looper.getMainLooper());
    }

    private void initRecyclerView() {
        messages = new ArrayList<>();
        adapter = new ChatMessageAdapter(messages);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(adapter);
    }

    private void loadMessages() {
        // 这里可以加载历史消息
    }

    private void sendMessage() {
        String messageText = binding.editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            addMessage(new ChatMessage(messageText, false));
            binding.editTextMessage.setText("");

            ZhipuAIService.sendMessage(messageText, new ZhipuAIService.AIResponseCallback() {
                @Override
                public void onResponse(String response) {
                    mainHandler.post(() -> addMessage(new ChatMessage(response, true)));
                }

                @Override
                public void onError(String error) {
                    mainHandler.post(() -> addMessage(new ChatMessage("Error: " + error, true)));
                }
            });
        }
    }

    private void addMessage(ChatMessage message) {
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
    }
}