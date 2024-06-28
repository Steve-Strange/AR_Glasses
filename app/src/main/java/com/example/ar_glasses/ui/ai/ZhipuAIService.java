package com.example.ar_glasses.ui.ai;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.service.v4.model.*;

import java.util.ArrayList;
import java.util.List;

public class ZhipuAIService {
    private static final String API_KEY = "a6784a470ff9e2ff79be562a17d7e056";
    private static final String API_SECRET = "XusmvF5KEM9IrYG4";
    private static final ClientV4 client = new ClientV4.Builder(API_KEY, API_SECRET).build();

    public interface AIResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public static void sendMessage(String message, AIResponseCallback callback) {
        new Thread(() -> {
            try {
                List<com.zhipu.oapi.service.v4.model.ChatMessage> messages = new ArrayList<>();
                messages.add(new com.zhipu.oapi.service.v4.model.ChatMessage(ChatMessageRole.USER.value(), message));

                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model("glm-4")
                        .messages(messages)
                        .stream(true)
                        .build();

                ModelApiResponse response = client.invokeModelApi(chatCompletionRequest);

                if (response.isSuccess()) {
                    StringBuilder fullResponse = new StringBuilder();
                    response.getFlowable().blockingForEach(chunk -> {
                        String content = chunk.getChoices().get(0).getDelta().getContent();
                        if (content != null) {
                            fullResponse.append(content);
                        }
                    });
                    callback.onResponse(fullResponse.toString());
                } else {
                    callback.onError("Error: " + response.getMsg());
                }
            } catch (Exception e) {
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }
}