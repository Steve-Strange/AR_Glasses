package com.example.ar_glasses.ui.ai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ar_glasses.databinding.FragmentAiBinding;

public class AiFragment extends Fragment {

    private FragmentAiBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AiViewModel aiViewModel =
                new ViewModelProvider(this).get(AiViewModel.class);

        binding = FragmentAiBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textAi;
        aiViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}