package com.phaseii.rxm.roomies.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Snehankur on 4/27/2015.
 */
public class RoomiesScrollView extends ScrollView {
    private OnScrollViewListener mOnScrollViewListener;

    public RoomiesScrollView(Context context) {
        super(context);
    }

    public RoomiesScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomiesScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        mOnScrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public interface OnScrollViewListener {
        void onScrollChanged(RoomiesScrollView v, int l, int t, int oldl, int oldt);
    }

}
