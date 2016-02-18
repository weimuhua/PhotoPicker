package me.com.photopicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import me.com.photopicker.R;
import me.com.photopicker.model.PhotoDir;

public class AlbumAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private int mSelectDir;
    private List<PhotoDir> mPhotoDirList;

    public AlbumAdapter(Context cxt) {
        mContext = cxt;
        mInflater = LayoutInflater.from(mContext);
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
        AlbumViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.photo_dir_layout, parent, false);
            holder = new AlbumViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        if (holder != null) {
            setAlbumView(holder, position);
        }
        return convertView;
    }

    public void setSelectDir(int position) {
        if (position >= 0 && position <= mPhotoDirList.size()) {
            mSelectDir = position;
            notifyDataSetChanged();
        }
    }

    private void setAlbumView(AlbumViewHolder holder, int position) {
        PhotoDir photoDir = mPhotoDirList.get(position);
        if (mSelectDir == position) {
            holder.albumSelected.setVisibility(View.VISIBLE);
        } else {
            holder.albumSelected.setVisibility(View.GONE);
        }

        holder.albumNameTv.setText(photoDir.name);
        holder.albumSizeTv.setText(mContext.getString(R.string.album_size, photoDir.photoList.size()));

        Glide.with(mContext)
                .load(photoDir.coverPath)
                .placeholder(holder.albumThumbImg.getDrawable())
                .error(R.mipmap.default_error)
                .into(holder.albumThumbImg);
    }

    private class AlbumViewHolder {
        public ImageView albumThumbImg;
        public TextView albumNameTv;
        public TextView albumSizeTv;
        public ImageView albumSelected;

        public AlbumViewHolder(View view) {
            albumThumbImg = (ImageView) view.findViewById(R.id.photo_album_cover);
            albumNameTv = (TextView) view.findViewById(R.id.photo_album_name);
            albumSizeTv = (TextView) view.findViewById(R.id.photo_album_photo_count);
            albumSelected = (ImageView) view.findViewById(R.id.photo_album_selected);
        }
    }
}
