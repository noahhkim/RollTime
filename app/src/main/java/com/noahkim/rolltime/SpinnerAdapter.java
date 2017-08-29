package com.noahkim.rolltime;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by noahkim on 8/28/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;
        ViewHolder mBeltViewHolder;

        if (view == null) {
            // we don't have a view so create one by inflating the layout
        }
        return null;
    }

    private static class ViewHolder {
        ImageView mBeltSpinnerView;
    }
}
