package com.example.ar_glasses.ui.ai;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.R;
import com.example.ar_glasses.data.ChatDatabaseHelper;
import com.example.ar_glasses.databinding.FragmentAiBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AiFragment extends Fragment implements ChatCardAdapter.OnItemClickListener {

    private FragmentAiBinding binding;
    private List<ChatCard> ChatCards;
    private ChatCardAdapter adapter;
    private ChatDatabaseHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new ChatDatabaseHelper(getContext());

        initRecyclerView();
        loadChatCards(); // 确保加载对话卡片

        FloatingActionButton fab = root.findViewById(R.id.fab_add_chat);
        fab.setOnClickListener(view -> startNewChat());

        return root;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.recyclerViewChatCards;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatCards = new ArrayList<>();
        adapter = new ChatCardAdapter(ChatCards);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    private void loadChatCards() {
        ChatCards.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("conversations", null, null, null, null, null, "last_message_time DESC");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("id"));
            String topic = cursor.getString(cursor.getColumnIndex("topic"));
            long lastMessageTime = cursor.getLong(cursor.getColumnIndex("last_message_time"));
            ChatCards.add(new ChatCard(topic, lastMessageTime, id));
        }
        cursor.close();
        Collections.sort(ChatCards, (o1, o2) -> Long.compare(o2.getTime(), o1.getTime()));
        adapter.notifyDataSetChanged();
    }

    private void startNewChat() {
        long newConversationId = System.currentTimeMillis(); // 用当前时间戳作为新的对话ID
        String newConversationTopic = "新对话";

        // 将新对话持久化到数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", newConversationId);
        values.put("topic", newConversationTopic);
        values.put("last_message_time", newConversationId);
        db.insert("conversations", null, values);

        // 重新加载对话卡片
        loadChatCards();
    }


    @Override
    public void onItemClick(ChatCard chatCard) {
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra("TOPIC", chatCard.getTopic());
        intent.putExtra("CONVERSATION_ID", chatCard.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
