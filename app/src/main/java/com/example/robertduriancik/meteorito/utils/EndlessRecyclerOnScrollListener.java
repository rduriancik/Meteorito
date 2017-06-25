package com.example.robertduriancik.meteorito.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by robert on 23.6.2017.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScroll";

    private static final int VISIBLE_THRESHOLD = 1;

    private int mPreviousTotalItemCount = 0;
    private boolean mLoading = true;
    private LinearLayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int totalItemCount = mLayoutManager.getItemCount();
        int visibleItemCount = recyclerView.getChildCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        Log.d(TAG, "onScrolled: mLoading " + mLoading + " total " + totalItemCount + " previousTotal " + mPreviousTotalItemCount);

        if (totalItemCount < mPreviousTotalItemCount) {
            this.mPreviousTotalItemCount = 0;
            if (totalItemCount == 0) {
                this.mLoading = true;
            }
        }
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        if (!mLoading && (firstVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount - visibleItemCount) {
            onLoadMore(totalItemCount);
            mLoading = true;
        }
    }

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean mLoading) {
        this.mLoading = mLoading;
    }

    public abstract void onLoadMore(int totalItemsCount);
}
