package com.example.ar_glasses.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ar_glasses.R;
import com.example.ar_glasses.databinding.FragmentHomeBinding;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TutorialAdapter tutorialAdapter;
    private List<TutorialItem> tutorials;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTutorials();
        setupRecyclerView();
    }

    private void initTutorials() {
        tutorials = Arrays.asList(
                new TutorialItem(1, R.drawable.tutorial1, "Look and Ask with Meta AI", "Content for tutorial 1"),
                new TutorialItem(2, R.drawable.tutorial2, "Say \"Hey Meta\" to learn and create", "Content for tutorial 2"),
                new TutorialItem(3, R.drawable.tutorial3, "Learn to take photos and videos", "Content for tutorial 3"),
                new TutorialItem(4, R.drawable.tutorial4, "Tutorial 4", "Content for tutorial 4")
        );
    }

    private void setupRecyclerView() {
        tutorialAdapter = new TutorialAdapter(tutorials, item -> {
            Intent intent = new Intent(getActivity(), TutorialDetailActivity.class);
            intent.putExtra(TutorialDetailActivity.EXTRA_TUTORIAL_ID, item.getId());
            intent.putExtra(TutorialDetailActivity.EXTRA_TUTORIAL_TITLE, item.getTitle());
            intent.putExtra(TutorialDetailActivity.EXTRA_TUTORIAL_CONTENT, item.getContent());
            startActivity(intent);
        });

        binding.tutorialGrid.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.tutorialGrid.setAdapter(tutorialAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}