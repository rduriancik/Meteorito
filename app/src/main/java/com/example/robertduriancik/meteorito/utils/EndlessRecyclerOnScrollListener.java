package com.example.robertduriancik.meteorito.utils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by robert on 23.6.2017.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessRecyclerOnScroll";

    // The minimum amount of items to have below your current scroll position
    // before mLoading more.
    private static final int VISIBLE_THRESHOLD = 1;
    // The total number of items in the dataset after the last load
    private int mPreviousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean mLoading = true;
    private LinearLayoutManager mLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int totalItemCount = mLayoutManager.getItemCount();
        int visibleItemCount = recyclerView.getChildCount();
        int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < mPreviousTotalItemCount) {
            this.mPreviousTotalItemCount = 0;
            if (totalItemCount == 0) {
                this.mLoading = true;
            }
        }
        // If it’s still mLoading, we check to see if the dataset count has
        // changed, if so we conclude it has finished mLoading and update the total item count.
        if (mLoading && (totalItemCount > mPreviousTotalItemCount)) {
            mLoading = false;
            mPreviousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently mLoading, we check to see if we have breached
        // the VISIBLE_THRESHOLD and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!mLoading && (firstVisibleItemPosition + VISIBLE_THRESHOLD) > totalItemCount - visibleItemCount) {
            onLoadMore(totalItemCount);
            mLoading = true;
        }
    }

    // Defines the process for actually mLoading more data
    public abstract void onLoadMore(int totalItemsCount);
}
