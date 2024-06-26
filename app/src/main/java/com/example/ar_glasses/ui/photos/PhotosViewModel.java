package com.example.ar_glasses.ui.photos;

import android.app.Application;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ar_glasses.PhotoGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class PhotosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<PhotoGroup>> photos;

    public PhotosViewModel(@NonNull Application application) {
        super(application);
        photos = new MutableLiveData<>();
        loadPhotos();
    }

    public LiveData<List<PhotoGroup>> getPhotos() {
        return photos;
    }

    private void loadPhotos() {
        TreeMap<String, List<String>> groupedPhotos = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN
        };
        String selection = null;  // 移除 DCIM 文件夹的限制
        String[] selectionArgs = null;  // 移除 DCIM 文件夹的限制
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        try (Cursor cursor = getApplication().getContentResolver().query(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder)) {

            if (cursor != null) {
                Log.d("PhotosViewModel", "Cursor count: " + cursor.getCount());
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);

                while (cursor.moveToNext()) {
                    String photoPath = cursor.getString(dataColumn);
                    long dateTaken = cursor.getLong(dateColumn);
                    String dateGroup = dateFormat.format(new Date(dateTaken));

                    groupedPhotos.computeIfAbsent(dateGroup, k -> new ArrayList<>()).add(photoPath);
                }
            }
        }

        List<PhotoGroup> photoGroups = new ArrayList<>();
        for (String date : groupedPhotos.keySet()) {
            photoGroups.add(new PhotoGroup(date, groupedPhotos.get(date)));
        }

        Log.d("PhotosViewModel", "Photo groups count: " + photoGroups.size());
        photos.postValue(photoGroups);
    }
}