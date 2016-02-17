package me.com.photopicker.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import baidu.com.commontools.threadpool.MhThreadPool;
import me.com.photopicker.R;
import me.com.photopicker.adapter.ThumbnailAdapter;
import me.com.photopicker.model.Photo;
import me.com.photopicker.model.PhotoDir;
import me.com.photopicker.utils.PhotoInfoUtils;

public class PickPhotoActivity extends AppCompatActivity {

    private static final String TAG = "PickPhotoActivity";

    private static final int MSG_LOAD_PHOTOS_DONE = 1;

    private Context mContext;

    private RecyclerView mRecyclerView;
    private ThumbnailAdapter mAdapter;
    private List<PhotoDir> mPhotoDirList;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_PHOTOS_DONE:
                    if (mPhotoDirList != null) {
                        List<Photo> allPhotoList
                                = mPhotoDirList.get(PhotoInfoUtils.ALL_PHOTOS_INDEX).photoList;
                        mAdapter.setData(allPhotoList);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    break;
            }
            return false;
        }
    });

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
    }

    private void initData() {
        mAdapter = new ThumbnailAdapter(this);
        MhThreadPool.getInstance().addUiTask(new Runnable() {
            @Override
            public void run() {
                mPhotoDirList = PhotoInfoUtils.getPhotos(mContext);
                mHandler.sendEmptyMessage(MSG_LOAD_PHOTOS_DONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

