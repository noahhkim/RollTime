package com.noahkim.rolltime;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by noahkim on 8/28/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
//    private int[] mBeltArray = {
//            R.drawable.ic_bjj_white_belt,
//            R.drawable.ic_bjj_blue_belt,
//            R.drawable.ic_bjj_purple_belt,
//            R.drawable.ic_bjj_brown_belt,
//            R.drawable.ic_bjj_black_belt
//    };

    private int[] mBeltArray;

    public SpinnerAdapter(Context context, int[] array) {
        mContext = context;
        mBeltArray = array;
    }

    @Override
    public int getCount() {
        return mBeltArray.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View itemView = view;
        ViewHolder mBeltSpinnerHolder;

        if (view == null) {
            // we don't have a view so create one by inflating the layout
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = mInflater.inflate(R.layout.spinner_layout, parent, false);

            mBeltSpinnerHolder = new ViewHolder();
            mBeltSpinnerHolder.mBeltSpinnerView = itemView.findViewById(R.id.spinner_belt_image);
            itemView.setTag(mBeltSpinnerHolder);
        } else {
            // we have a view so get the tagged view
            mBeltSpinnerHolder = (ViewHolder) itemView.getTag();
        }

        // display current image
        mBeltSpinnerHolder.mBeltSpinnerView.setImageResource(mBeltArray[position]);

        return itemView;
    }

    private static class ViewHolder {
        ImageView mBeltSpinnerView;
    }
}
