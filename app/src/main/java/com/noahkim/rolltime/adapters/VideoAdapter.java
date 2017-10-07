package com.noahkim.rolltime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.noahkim.rolltime.R;
import com.noahkim.rolltime.data.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Noah on 10/5/2017.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoItemViewHolder> {
    private Context mContext;
    private List<Video> mVideos;

    public VideoAdapter(Context context, List<Video> videos) {
        mContext = context;
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
        Timber.d("Videos from video adapter: " + mVideos.size());
        return mVideos.size();
    }
//
//    public void setVideos(List<Video> videos) {
//        mVideos = videos;
//        notifyDataSetChanged();
//    }

    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_title)
        TextView mVideoTitleView;

        public VideoItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
