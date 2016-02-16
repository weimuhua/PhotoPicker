package me.com.photopicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import me.com.photopicker.model.Photo;
import me.com.photopicker.model.PhotoDir;
import me.com.photopicker.utils.PhotoInfoUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PhotoPickerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }

    private void test() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                List<PhotoDir> photoDirList = PhotoInfoUtils.getPhotos(MainActivity.this);
                if (photoDirList == null) {
                    Log.d(TAG, "photoDirList == NULL");
                    return;
                }

                PhotoDir allPhotoDir = photoDirList.get(PhotoInfoUtils.ALL_PHOTOS_INDEX);
                Log.d(TAG, String.format("allPhotoDir id : %s name : %s coverPath : %s photoSize : %d",
                        allPhotoDir.id, allPhotoDir.name, allPhotoDir.coverPath, allPhotoDir.photoList.size()));
                for (Photo photo : allPhotoDir.photoList) {
                    Log.d(TAG, "photo id : " + photo.id + " , path : " + photo.path);
                }
            }
        });
    }
}
