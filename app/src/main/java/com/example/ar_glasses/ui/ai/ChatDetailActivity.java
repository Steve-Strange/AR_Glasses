package com.example.ar_glasses.ui.ai;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.ar_glasses.databinding.ActivityChatDetailBinding;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.zhipu.oapi.service.v4.model.Choice;
import com.zhipu.oapi.service.v4.model.ModelData;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private List<com.example.ar_glasses.ui.ai.ChatMessage> messages;
    private ChatMessageAdapter adapter;
    private ClientV4 client;
    private static final String API_KEY = "a6784a470ff9e2ff79be562a17d7e056.XusmvF5KEM9IrYG4";
    private static final String API_SECRET = "your_api_secret_here"; // Replace with your actual API secret
    private static final String TAG = "ChatDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String topic = getIntent().getStringExtra("TOPIC");
        setTitle(topic);

        initRecyclerView();
        initZhipuAIClient();
        loadMessages();

        binding.sendButton.setOnClickListener(v -> sendMessage());
    }

    private void initRecyclerView() {
        messages = new ArrayList<>();
        adapter = new ChatMessageAdapter(messages);
        binding.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewMessages.setAdapter(adapter);
    }

    private void initZhipuAIClient() {
        client = new ClientV4.Builder(API_KEY, API_SECRET).build();
    }

    private void loadMessages() {
        // 这里应该从数据库加载实际的消息
        // 这只是一个示例
        messages.add(new com.example.ar_glasses.ui.ai.ChatMessage("Hello!", false));
        messages.add(new com.example.ar_glasses.ui.ai.ChatMessage("Hi there! How can I help you?", true));
        adapter.notifyDataSetChanged();
    }

    private void sendMessage() {
        String messageText = binding.editTextMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            messages.add(new com.example.ar_glasses.ui.ai.ChatMessage(messageText, false));
            adapter.notifyItemInserted(messages.size() - 1);
            binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
            binding.editTextMessage.setText("");

            // 发送消息到智谱AI平台并获取回复
            new Thread(() -> {
                try {
                    List<ChatMessage> chatMessages = new ArrayList<>();
                    chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), messageText));

                    String requestId = "request-" + UUID.randomUUID().toString();

                    ChatCompletionRequest request = ChatCompletionRequest.builder()
                            .model("glm-4")
                            .messages(chatMessages)
                            .stream(false)
                            .invokeMethod("invoke")
                            .requestId(requestId)
                            .build();

                    Log.d(TAG, "Sending request: " + request);

                    ModelApiResponse response = client.invokeModelApi(request);

                    Log.d(TAG, "Received response: " + response);

                    if (response != null && response.isSuccess()) {
                        ModelData data = response.getData();
                        if (data != null) {
                            List<Choice> choices = data.getChoices();
                            if (choices != null && !choices.isEmpty()) {
                                Choice choice = choices.get(0);
                                ChatMessage message = choice.getMessage();
                                if (message != null && message.getContent() != null) {
                                    String aiReply = message.getContent().toString().trim();
                                    Log.d(TAG, "AI reply: " + aiReply);

                                    runOnUiThread(() -> {
                                        if (!aiReply.isEmpty()) {
                                            messages.add(new com.example.ar_glasses.ui.ai.ChatMessage(aiReply, true));
                                            adapter.notifyItemInserted(messages.size() - 1);
                                            binding.recyclerViewMessages.smoothScrollToPosition(messages.size() - 1);
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "No message or content in choice");
                                }
                            } else {
                                Log.d(TAG, "No choices in response data");
                            }
                        } else {
                            Log.d(TAG, "No data in response");
                        }
                    } else if (response != null) {
                        Log.d(TAG, "Response not successful: " + response.getMsg());
                    } else {
                        Log.d(TAG, "Response is null");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while sending message", e);
                }
            }).start();
        }
    }
}
