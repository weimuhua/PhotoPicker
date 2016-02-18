package me.com.photopicker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import me.com.photopicker.R;
import me.com.photopicker.adapter.PhotoAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhotoPickerMain";

    public static final int PICK_PHOTO_REQ_CODE = 1001;
    public static final String EXTRA_PICK_PHOTOS = "extra_pick_photos";

    private Context mContext;

    private Button mButton;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private PhotoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
        }
        return true;
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.pick_photo_button);
        mButton.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.home_menu_title));
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mAdapter = new PhotoAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == mButton) {
            intent = new Intent(mContext, PickPhotoActivity.class);
        }
        if (intent != null) {
            startActivityForResult(intent, PICK_PHOTO_REQ_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO_REQ_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    List<String> photoList = data.getStringArrayListExtra(EXTRA_PICK_PHOTOS);
                    if (photoList != null) {
                        Toast.makeText(mContext,
                                getString(R.string.pick_photos_size, photoList.size()),
                                Toast.LENGTH_SHORT).
                                show();

                        mAdapter.setPhotoList(photoList);
                    }
                }
                break;
        }
    }
}
