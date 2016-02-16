package me.com.photopicker.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.utils.BitmapUtils;
import me.com.photopicker.R;
import me.com.photopicker.model.PhotoDir;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;

public class PhotoInfoUtils {

    private static final String TAG = "PhotoInfoUtils";

    public static final int ALL_PHOTOS_INDEX = 0;

    private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String IMAGE_PNG = "image/png";
    private static final String IMAGE_GIF = "image/gif";
    private static final String SORT = MediaStore.Images.Media.DATE_ADDED + " DESC";

    private static final String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
    };

    public static List<PhotoDir> getPhotos(Context cxt) {
        ContentResolver resolver = cxt.getContentResolver();

        String selection = MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?";
        String selectionArgs[] = new String[]{
                IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF
        };

        Cursor cursor = resolver.query(IMAGE_URI, IMAGE_PROJECTION, selection, selectionArgs, SORT);
        if (cursor == null) return null;

        List<PhotoDir> photoDirs = parseData(cxt, cursor);
        cursor.close();

        return photoDirs;
    }

    private static List<PhotoDir> parseData(Context cxt, Cursor cursor) {
        List<PhotoDir> photoDirs = new ArrayList<>();

        PhotoDir allPhotoDir = new PhotoDir();
        allPhotoDir.name = cxt.getString(R.string.all_photo_name);
        allPhotoDir.id = "ALL_PHOTO";

        while (cursor.moveToNext()) {
            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String bucketId = cursor.getString(cursor.getColumnIndexOrThrow(BUCKET_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(DATA));

            if (!BitmapUtils.isImgCorrupted(path)) {
                PhotoDir dir = new PhotoDir();
                dir.id = bucketId;
                dir.name = name;

                if (!photoDirs.contains(dir)) {
                    dir.coverPath = path;
                    dir.addPhoto(imageId, path);
                    dir.date = cursor.getLong(cursor.getColumnIndexOrThrow(DATE_ADDED));
                } else {
                    photoDirs.get(photoDirs.indexOf(dir)).addPhoto(imageId, path);
                }

                allPhotoDir.addPhoto(imageId, path);
            }
        }

        List<String> allPhotoPaths = allPhotoDir.getPhotoPaths();
        if (allPhotoPaths.size() > 0) {
            allPhotoDir.coverPath = allPhotoPaths.get(0);
        }
        photoDirs.add(ALL_PHOTOS_INDEX, allPhotoDir);

        return photoDirs;
    }
}
