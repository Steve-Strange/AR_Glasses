package com.example.ar_glasses.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ar_glasses.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupSettingItems();

        return root;
    }

    private void setupSettingItems() {
        binding.glassesPrivacy.getRoot().setOnClickListener(v -> navigateToSubSetting("Glasses & privacy"));
        binding.voice.getRoot().setOnClickListener(v -> navigateToSubSetting("Voice"));
        binding.camera.getRoot().setOnClickListener(v -> navigateToSubSetting("Camera"));
        // Add more click listeners for other setting items
    }

    private void navigateToSubSetting(String settingName) {
        // TODO: Implement navigation to sub-setting
        Toast.makeText(getContext(), "Navigating to " + settingName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}