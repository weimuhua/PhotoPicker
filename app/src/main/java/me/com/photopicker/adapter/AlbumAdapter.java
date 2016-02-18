package me.com.photopicker.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import me.com.photopicker.model.PhotoDir;

public class AlbumAdapter extends BaseAdapter {

    private Context mContext;
    private List<PhotoDir> mPhotoDirList;

    public AlbumAdapter(Context cxt) {
        mContext = cxt;
        mPhotoDirList = new ArrayList<>();
    }

    public void setPhotoDir(List<PhotoDir> photoDirList) {
        if (photoDirList != null) {
            mPhotoDirList.clear();
            mPhotoDirList.addAll(photoDirList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mPhotoDirList.size();
    }

    @Override
    public PhotoDir getItem(int position) {
        return mPhotoDirList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class AlbumViewHolder {

    }
}
