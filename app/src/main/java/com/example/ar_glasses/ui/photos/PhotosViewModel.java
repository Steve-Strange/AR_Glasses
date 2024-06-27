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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class PhotosViewModel extends AndroidViewModel {

    private MutableLiveData<List<PhotoGroup>> photoGroups = new MutableLiveData<>();

    private final Application application;

    public PhotosViewModel(Application application) {
        super(application);
        this.application = application;
    }

    public LiveData<List<PhotoGroup>> getPhotoGroups() {
        return photoGroups;
    }

    public void loadImages() {
        new Thread(this::queryImages).start();
    }

    private void queryImages() {
        TreeMap<String, List<MediaStoreImage>> groupedPhotos = new TreeMap<>((d1, d2) -> d2.compareTo(d1));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String targetFolder = "%Pictures/Gallery/owner/test";
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " ASC";
        String selection = MediaStore.Files.FileColumns.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{targetFolder + "%"};
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.DISPLAY_NAME,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE
        };

        try (Cursor cursor = application.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
                int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED);
                int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                int mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    long dateAdded = cursor.getLong(dateAddedColumn);
                    String displayName = cursor.getString(displayNameColumn);
                    String filePath = cursor.getString(dataColumn);
                    String mimeType = cursor.getString(mimeTypeColumn);
                    String dateGroup = dateFormat.format(new Date(dateAdded * 1000)); // Convert seconds to milliseconds

                    Uri contentUri = ContentUris.withAppendedId(
                            MediaStore.Files.getContentUri("external"),
                            id
                    );

                    boolean isVideo = mimeType != null && mimeType.startsWith("video");
                    MediaStoreImage media = new MediaStoreImage(id, displayName, new Date(dateAdded * 1000), contentUri, isVideo);

                    groupedPhotos.computeIfAbsent(dateGroup, k -> new ArrayList<>()).add(media);
                }
            } else {
                Log.e("PhotosViewModel", "Cursor is null");
            }
        } catch (Exception e) {
            Log.e("PhotosViewModel", "Error querying images", e);
        }

        List<PhotoGroup> photoGroupList = new ArrayList<>();
        for (String date : groupedPhotos.keySet()) {
            List<MediaStoreImage> mediaList = groupedPhotos.get(date);
            photoGroupList.add(new PhotoGroup(date, mediaList));
        }

        photoGroups.postValue(photoGroupList);
    }
}
