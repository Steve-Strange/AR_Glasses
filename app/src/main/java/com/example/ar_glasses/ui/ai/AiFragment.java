package com.example.ar_glasses.ui.ai;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.databinding.FragmentAiBinding;

import java.util.ArrayList;
import java.util.List;

public class AiFragment extends Fragment implements ChatCardAdapter.OnItemClickListener {

    private FragmentAiBinding binding;
    private List<ChatCard> ChatCards;
    private ChatCardAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initRecyclerView();
        loadChatCards();

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
        // 这里应该从数据库或API加载实际的聊天主题
        // 这只是一个示例
        ChatCards.add(new ChatCard("AI基础知识", "2024-06-28 10:00"));
        ChatCards.add(new ChatCard("机器学习算法", "2024-06-28 11:30"));
        ChatCards.add(new ChatCard("自然语言处理", "2024-06-28 14:15"));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(ChatCard chatCard) {
        Intent intent = new Intent(getContext(), ChatDetailActivity.class);
        intent.putExtra("TOPIC", chatCard.getTopic());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
