package com.example.ar_glasses.ui.photos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.databinding.FragmentPhotosBinding;

public class PhotosFragment extends Fragment {

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 0x1045;
    private FragmentPhotosBinding binding;
    private PhotosViewModel photosViewModel;
    private PhotoAdapter photoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        if (haveStoragePermission()) {
            showImages();
        } else {
            requestPermission();
        }

        return root;
    }

    private void showImages() {
        photosViewModel.loadImages();
        photosViewModel.getPhotoGroups().observe(getViewLifecycleOwner(), photoGroups -> {
            if (photoGroups != null && !photoGroups.isEmpty()) {
                photoAdapter.setPhotoGroups(photoGroups);
            } else {
                Toast.makeText(getContext(), "No images found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean haveStoragePermission() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (!haveStoragePermission()) {
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            requestPermissions(permissions, READ_EXTERNAL_STORAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImages();
            } else {
                Toast.makeText(getContext(), "Permission denied. Cannot load images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}