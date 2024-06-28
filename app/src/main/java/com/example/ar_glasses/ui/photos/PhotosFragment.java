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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ar_glasses.databinding.FragmentPhotosBinding;

import java.util.List;

public class PhotosFragment extends Fragment {

    private static final int REQUEST_PERMISSIONS = 0x1045;
    private FragmentPhotosBinding binding;
    private PhotosViewModel photosViewModel;
    private PhotoAdapter photoAdapter;

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_AUDIO, // For music and audio
            Manifest.permission.READ_MEDIA_VIDEO, // For videos
            Manifest.permission.READ_MEDIA_IMAGES // For images (optional, as it's covered by READ_EXTERNAL_STORAGE on older devices)
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        photosViewModel = new ViewModelProvider(this).get(PhotosViewModel.class);

        ZoomableRecyclerView recyclerView = binding.recyclerView;
        photoAdapter = new PhotoAdapter();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return photoAdapter.isHeader(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(photoAdapter);

        if (allPermissionsGranted()) {
            showImages();
        } else {
            requestPermissions();
        }

        return root;
    }

    private void showImages() {
        photosViewModel.loadImages();
        photosViewModel.getPhotoGroups().observe(getViewLifecycleOwner(), new Observer<List<PhotoGroup>>() {
            @Override
            public void onChanged(List<PhotoGroup> photoGroups) {
                if (photoGroups != null && !photoGroups.isEmpty()) {
                    photoAdapter.setPhotoGroups(photoGroups);
                } else {
                    Toast.makeText(getContext(), "No images found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestPermissions() {
        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (allPermissionsGranted()) {
                showImages();
            } else {
                Toast.makeText(getContext(), "Permissions not granted. Cannot load media.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
