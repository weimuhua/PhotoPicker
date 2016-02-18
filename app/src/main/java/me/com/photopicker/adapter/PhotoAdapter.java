package me.com.photopicker.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import baidu.com.commontools.utils.MobileInfo;
import me.com.photopicker.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Activity mActivity;
    private List<String> mPhotoList;

    public PhotoAdapter(Activity activity) {
        mActivity = activity;
        mPhotoList = new ArrayList<>();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.thumbnail_layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.setPhoto(mPhotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public void setPhotoList(List<String> photoList) {
        if (photoList != null) {
            mPhotoList.clear();
            mPhotoList.addAll(photoList);
            notifyDataSetChanged();
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView photo;
        public ImageView selected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            int width = MobileInfo.getScreenMetrics(mActivity).widthPixels / 3;
            photo = (ImageView) itemView.findViewById(R.id.photo_thumbview);
            photo.setLayoutParams(new RelativeLayout.LayoutParams(width, width));

            selected = (ImageView) itemView.findViewById(R.id.photo_thumbview_selected);
            selected.setVisibility(View.GONE);
        }

        public void setPhoto(String path) {
            Uri uri = new Uri.Builder().scheme("file").path(path).build();

            Glide.with(mActivity)
                    .load(uri)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .thumbnail(0.3f)
                    .into(photo);
        }
    }
}
