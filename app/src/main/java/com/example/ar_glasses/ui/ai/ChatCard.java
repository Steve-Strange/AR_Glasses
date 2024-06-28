package com.example.ar_glasses.ui.ai;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatCard {
    private String topic;
    private long time;
    private long id;

    public ChatCard(String topic, long time, long id) {
        this.topic = topic;
        this.time = time;
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public long getTime() {
        return time;
    }

    public long getId() {
        return id;
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date(time));
    }
}
