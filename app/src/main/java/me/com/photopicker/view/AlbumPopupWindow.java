package me.com.photopicker.view;

import android.content.Context;
import android.support.v7.widget.ListPopupWindow;
import android.util.AttributeSet;

import java.util.List;

import baidu.com.commontools.utils.MobileInfo;
import me.com.photopicker.adapter.AlbumAdapter;
import me.com.photopicker.model.PhotoDir;

public class AlbumPopupWindow extends ListPopupWindow {

    private AlbumAdapter mAlbumAdapter;

    public AlbumPopupWindow(Context context) {
        super(context);
        initView(context);
    }

    public AlbumPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AlbumPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context cxt) {
        mAlbumAdapter = new AlbumAdapter(cxt);
        mAlbumAdapter.setSelectDir(0);
        setAdapter(mAlbumAdapter);

        int width = MobileInfo.getScreenMetrics(cxt).widthPixels;
        setContentWidth(width);
        setHeight(width);
        setModal(true);
    }

    public void setPhotoDirList(List<PhotoDir> photoDirList) {
        mAlbumAdapter.setPhotoDir(photoDirList);
    }

    public void setSelectDir(int position) {
        mAlbumAdapter.setSelectDir(position);
    }

    public PhotoDir getSelectPhotoDir(int position) {
        return mAlbumAdapter.getItem(position);
    }
}
