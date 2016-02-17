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
import me.com.photopicker.model.Photo;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailViewHolder> {

    private Activity mActivity;
    private List<Photo> mPhotoList;
    private List<String> mSelectList;

    public ThumbnailAdapter(Activity activity) {
        mActivity = activity;
        mSelectList = new ArrayList<>();
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.thumbnail_layout, parent, false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        if (mPhotoList != null) {
            holder.setThumbnail(mPhotoList.get(position));
            holder.thumbnail.setOnClickListener(holder);
        }
    }

    @Override
    public int getItemCount() {
        return mPhotoList != null ? mPhotoList.size() : 0;
    }

    public void setData(List<Photo> photoList) {
        if (photoList != null) {
            mPhotoList = photoList;
            notifyDataSetChanged();
        }
    }

    public List<String> getSelectList() {
        return mSelectList;
    }

    class ThumbnailViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView thumbnail;
        public ImageView selectedTip;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.photo_thumbview);
            selectedTip = (ImageView) itemView.findViewById(R.id.photo_thumbview_selected);
            int width = MobileInfo.getScreenMetrics(mActivity).widthPixels / 3;
            thumbnail.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        }

        public void setThumbnail(Photo photo) {
            Uri uri = new Uri.Builder().scheme("file").path(photo.path).build();

            Glide.with(mActivity)
                    .load(uri)
                    .placeholder(R.mipmap.default_error)
                    .thumbnail(0.3f)
                    .error(R.mipmap.default_error)
                    .into(thumbnail);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
