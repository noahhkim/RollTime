package com.noahkim.rolltime.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Noah on 10/5/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoItemViewHolder> {
    private List<Video> mVideos;
    private static final String LOG_TAG = VideoAdapter.class.getName();

    public VideoAdapter(List<Video> videos) {
        mVideos = videos;
    }

    @Override
    public VideoAdapter.VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_item, parent, false);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.VideoItemViewHolder holder, int position) {
        final Video currentVideo = mVideos.get(position);
        holder.mVideoTitleView.setText(currentVideo.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mVideos == null) {
            return 0;
        }
        Log.d(LOG_TAG, "Videos from video adapter: " + mVideos.size());
        return mVideos.size();
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
        notifyDataSetChanged();
    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_title)
        TextView mVideoTitleView;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
