package com.example.ar_glasses.ui.photos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ar_glasses.PhotoAdapter;
import com.example.ar_glasses.databinding.FragmentPhotosBinding;

public class PhotosFragment extends Fragment {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private FragmentPhotosBinding binding;
    private PhotosViewModel photosViewModel;
    private PhotoAdapter photoAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        photoAdapter = new PhotoAdapter();
        recyclerView.setAdapter(photoAdapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            loadPhotos();
        }

        return root;
    }

    private void loadPhotos() {
        Log.d("PhotosFragment", "Loading photos...");
        photosViewModel.getPhotos().observe(getViewLifecycleOwner(), photoGroups -> {
            if (photoGroups != null && !photoGroups.isEmpty()) {
                photoAdapter.setPhotoGroups(photoGroups);
                Log.d("PhotosFragment", "Loaded " + photoGroups.size() + " photo groups");
            } else {
                Log.e("PhotosFragment", "No photos loaded");
                Toast.makeText(getContext(), "No photos found", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}