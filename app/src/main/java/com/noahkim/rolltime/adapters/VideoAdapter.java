package com.noahkim.rolltime.adapters;

/**
 * Created by Noah on 10/5/2017.
 */

//public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoItemViewHolder> {
//    private Context mContext;
//    private List<Video> mVideos;
//
//    public VideoAdapter(Context context, List<Video> videos) {
//        mContext = context;
//        mVideos = videos;
//    }
//
//    @Override
//    public VideoAdapter.VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_item, parent, false);
//        return new VideoItemViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(VideoAdapter.VideoItemViewHolder holder, int position) {
//        final Video currentVideo = mVideos.get(position);
//        holder.mVideoTitleView.setText(currentVideo.getTitle());
//        Picasso.with(mContext)
//                .load(currentVideo.getThumbnail())
//                .into(holder.mThumbnailView);
//    }
//
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//    }
//
//    public void onSaveInstanceState(Bundle outState) {
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mVideos == null) {
//            return 0;
//        }
//        return mVideos.size();
//    }
//
//    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.video_thumbnail)
//        ImageView mThumbnailView;
//        @BindView(R.id.video_title)
//        TextView mVideoTitleView;
//
//        public VideoItemViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//    }
//}
