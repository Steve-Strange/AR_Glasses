package com.example.ar_glasses.ui.ai;

public class ChatMessage {
    private String content;
    private boolean isAi;

    public ChatMessage(String content, boolean isAi) {
        this.content = content;
        this.isAi = isAi;
    }

    public String getContent() {
        return content;
    }

    public boolean isAi() {
        return isAi;
    }
}