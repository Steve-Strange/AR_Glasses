package com.example.ar_glasses.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.R;
import com.example.ar_glasses.TutorialAdapter;
import com.example.ar_glasses.TutorialItem;
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
                new TutorialItem(R.drawable.tutorial1, "Look and Ask with Meta AI", "Content for tutorial 1"),
                new TutorialItem(R.drawable.tutorial2, "Say \"Hey Meta\" to learn and create", "Content for tutorial 2"),
                new TutorialItem(R.drawable.tutorial3, "Learn to take photos and videos", "Content for tutorial 3"),
                new TutorialItem(R.drawable.tutorial4, "Tutorial 4", "Content for tutorial 4")
        );
    }

    private void setupRecyclerView() {
        tutorialAdapter = new TutorialAdapter(tutorials, item -> {
            // Handle click on tutorial item
            // For example, navigate to a detail fragment:
            // NavHostFragment.findNavController(HomeFragment.this)
            //     .navigate(R.id.action_homeFragment_to_tutorialDetailFragment,
            //               bundleOf("tutorialContent", item.getContent()));
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