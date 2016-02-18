package me.com.photopicker.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private static final String TAG = "ThumbnailAdapter";

    private Activity mActivity;
    private List<Photo> mPhotoList;
    private ArrayList<String> mSelectList;
    private int[] mSelectTipId;

    public ThumbnailAdapter(Activity activity) {
        mActivity = activity;
        mSelectList = new ArrayList<>();
        mSelectTipId = new int[] {
                R.mipmap.photo_selected,
                R.mipmap.photo_unselected
        };
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.thumbnail_layout, parent, false);
        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder, position : " + position);

        if (mPhotoList != null) {
            holder.setThumbnail(mPhotoList.get(position), position);
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

    public ArrayList<String> getSelectList() {
        return mSelectList;
    }

    class ThumbnailViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView thumbnail;
        public ImageView selectedTip;
        private int position;

        public ThumbnailViewHolder(View itemView) {
            super(itemView);
            selectedTip = (ImageView) itemView.findViewById(R.id.photo_thumbview_selected);

            int width = MobileInfo.getScreenMetrics(mActivity).widthPixels / 3;
            thumbnail = (ImageView) itemView.findViewById(R.id.photo_thumbview);
            thumbnail.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
            thumbnail.setOnClickListener(this);
        }

        public void setThumbnail(Photo photo, int position) {
            this.position = position;

            Uri uri = new Uri.Builder().scheme("file").path(photo.path).build();

            Glide.with(mActivity)
                    .load(uri)
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .thumbnail(0.3f)
                    .into(thumbnail);
        }

        @Override
        public void onClick(View v) {
            Photo photo = mPhotoList.get(position);
            if (mSelectList.contains(photo.path)) {
                selectedTip.setImageResource(mSelectTipId[1]);
                mSelectList.remove(photo.path);
            } else {
                selectedTip.setImageResource(mSelectTipId[0]);
                mSelectList.add(photo.path);
            }
        }
    }
}
