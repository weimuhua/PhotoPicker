package me.com.photopicker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

    private Toolbar mToolbar;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pick_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        Log.d(TAG, "mAdapter.getSelectList() : " + mAdapter.getSelectList().size());
        if (mAdapter.getSelectList().isEmpty()) {
            setResult(RESULT_CANCELED, intent);
        } else {
            intent.putStringArrayListExtra(MainActivity.EXTRA_PICK_PHOTOS, mAdapter.getSelectList());
            setResult(RESULT_OK, intent);
        }

        int id = item.getItemId();
        if (id == R.id.action_finish) {
            finish();
        } else if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.pick_photos));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
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
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}

