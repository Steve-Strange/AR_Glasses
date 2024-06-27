package com.example.ar_glasses.ui.photos;

import android.app.Application;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class PhotosViewModel extends AndroidViewModel {

    private final MutableLiveData<List<PhotoGroup>> photoGroups;
    private final Application application;

    public PhotosViewModel(Application application) {
        super(application);
        this.application = application;
        photoGroups = new MutableLiveData<>();
    }

    public LiveData<List<PhotoGroup>> getPhotoGroups() {
        return photoGroups;
    }

    public void loadImages() {
        new Thread(this::queryImages).start();
    }

    private void queryImages() {
        TreeMap<String, List<MediaStoreImage>> groupedPhotos = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String targetFolder = "%Pictures/Gallery/owner/test";
        String sortOrder = MediaStore.Images.Media.DATE_TAKEN + " ASC"; // 尝试使用DATE_TAKEN进行降序排列
        String selection = MediaStore.Images.Media.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{targetFolder + "%"};
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA // 确保包含数据列
        };

        Log.d("PhotosViewModel", "Selection: " + selection);
        Log.d("PhotosViewModel", "SelectionArgs: " + Arrays.toString(selectionArgs));

        try (Cursor cursor = application.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection, // 添加查询条件
                selectionArgs, // 添加查询参数
                sortOrder
        )) {
            if (cursor != null) {
                Log.d("PhotosViewModel", "Cursor count: " + cursor.getCount());
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN);
                int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    long dateTaken = cursor.getLong(dateTakenColumn);
                    String displayName = cursor.getString(displayNameColumn);
                    String filePath = cursor.getString(dataColumn); // 获取文件路径
                    String dateGroup = dateFormat.format(new Date(dateTaken));

                    Log.d("PhotosViewModel", "Found image: " + displayName + " at " + filePath);

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                    );

                    MediaStoreImage image = new MediaStoreImage(id, displayName, new Date(dateTaken), contentUri);

                    groupedPhotos.computeIfAbsent(dateGroup, k -> new ArrayList<>()).add(image);
                }
            } else {
                Log.e("PhotosViewModel", "Cursor is null");
            }
        } catch (Exception e) {
            Log.e("PhotosViewModel", "Error querying images", e);
        }



        List<PhotoGroup> photoGroupList = new ArrayList<>();
        for (String date : groupedPhotos.keySet()) {
            photoGroupList.add(new PhotoGroup(date, groupedPhotos.get(date)));
        }

        Log.d("PhotosViewModel", "Photo groups count: " + photoGroupList.size());
        photoGroups.postValue(photoGroupList);
    }
}