package me.com.photopicker.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import me.com.photopicker.R;
import me.com.photopicker.model.Photo;
import me.com.photopicker.model.PhotoDir;
import me.com.photopicker.utils.PhotoInfoUtils;

public class PickPhotoActivity extends AppCompatActivity {

    private static final String TAG = "PickPhotoActivity";

    private Context mContext;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
    }

    private void initData() {
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                List<PhotoDir> photoDirList = PhotoInfoUtils.getPhotos(mContext);
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

