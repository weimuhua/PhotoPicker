package me.com.photopicker.activity;

import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.com.photopicker.R;
import me.com.photopicker.adapter.ThumbnailAdapter;
import me.com.photopicker.model.Photo;
import me.com.photopicker.model.PhotoDir;
import me.com.photopicker.utils.PhotoInfoUtils;
import me.com.photopicker.view.AlbumPopupWindow;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PickPhotoActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private static final String TAG = "PickPhotoActivity";

    private Context mContext;

    private Toolbar mToolbar;
    private TextView mChangeDirTv;
    private RecyclerView mRecyclerView;
    private AlbumPopupWindow mPopupWindow;

    private ThumbnailAdapter mAdapter;
    private List<PhotoDir> mPhotoDirList;

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

        mPopupWindow = new AlbumPopupWindow(mContext);
        mPopupWindow.setAnchorView(findViewById(R.id.pick_photo_anchor));
        mPopupWindow.setOnItemClickListener(this);

        mChangeDirTv = (TextView) findViewById(R.id.choose_dir_tv);
        mChangeDirTv.setOnClickListener(this);
    }

    private void initData() {
        mAdapter = new ThumbnailAdapter(this, mToolbar);

        Observable
                .create(new Observable.OnSubscribe<List<PhotoDir>>() {
                    @Override
                    public void call(Subscriber<? super List<PhotoDir>> subscriber) {
                        subscriber.onNext(PhotoInfoUtils.getPhotos(mContext));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<PhotoDir>>() {
                    @Override
                    public void call(List<PhotoDir> photoDirs) {
                        mPhotoDirList = photoDirs;
                        showPhotos();
                    }
                });
    }

    private void showPhotos() {
        if (mPhotoDirList != null) {
            List<Photo> allPhotoList
                    = mPhotoDirList.get(PhotoInfoUtils.ALL_PHOTOS_INDEX).photoList;
            mAdapter.setData(allPhotoList);
            mRecyclerView.setAdapter(mAdapter);
            mPopupWindow.setPhotoDirList(mPhotoDirList);
        } else {
            Toast.makeText(mContext, getString(R.string.can_not_get_photo), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mChangeDirTv) {
            mPopupWindow.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhotoDir selectDir = mPopupWindow.getSelectPhotoDir(position);

        mAdapter.setData(selectDir.photoList);
        mRecyclerView.scrollToPosition(0);

        mChangeDirTv.setText(mPopupWindow.getSelectPhotoDir(position).name);

        mPopupWindow.setSelectDir(position);
        mPopupWindow.getListView().smoothScrollToPosition(position);
        mPopupWindow.dismiss();
    }
}

