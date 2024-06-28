package com.example.ar_glasses.ui.ai;

public class ChatCard {
    private String topic;
    private String time;

    public ChatCard(String topic, String time) {
        this.topic = topic;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public String getTime() {
        return time;
    }
}