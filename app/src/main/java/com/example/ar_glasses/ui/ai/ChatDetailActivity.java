package com.example.ar_glasses.ui.ai;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ar_glasses.data.ChatDatabaseHelper;
import com.example.ar_glasses.databinding.ActivityChatDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {

    private ActivityChatDetailBinding binding;
    private List<ChatMessage> messages;
    private ChatMessageAdapter adapter;
    private Handler mainHandler;
    private ChatDatabaseHelper dbHelper;
    private long conversationId;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topic = getIntent().getStringExtra("TOPIC");
        setTitle(topic);

        dbHelper = new ChatDatabaseHelper(this);
        conversationId = getIntent().getLongExtra("CONVERSATION_ID", System.currentTimeMillis());

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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("messages", null, "conversation_id = ?", new String[]{String.valueOf(conversationId)}, null, null, "timestamp ASC");
        while (cursor.moveToNext()) {
            String content = cursor.getString(cursor.getColumnIndex("content"));
            boolean isAi = cursor.getInt(cursor.getColumnIndex("is_ai")) == 1;
            messages.add(new ChatMessage(content, isAi));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
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

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("conversation_id", conversationId);
        values.put("content", message.getContent());
        values.put("is_ai", message.isAi() ? 1 : 0);
        db.insert("messages", null, values);

        ContentValues conversationValues = new ContentValues();
        conversationValues.put("last_message_time", System.currentTimeMillis());
        db.update("conversations", conversationValues, "id = ?", new String[]{String.valueOf(conversationId)});
    }


}