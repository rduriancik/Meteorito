package com.example.robertduriancik.meteorito.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeteoriteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface onMeteoriteListAdapterEvents {
        void onItemClick(MeteoriteLanding meteoriteLanding);

        void onLoadMore();
    }

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private final List<MeteoriteLanding> mMeteoriteLandingList;
    private final onMeteoriteListAdapterEvents mListener;

    private boolean isLoading;
    private int lastVisibleItem;
    private int totalItemCount;

    public MeteoriteListAdapter(RecyclerView recyclerView, List<MeteoriteLanding> meteoriteLandingList, onMeteoriteListAdapterEvents listener) {
        this.mMeteoriteLandingList = meteoriteLandingList;
        this.mListener = listener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= lastVisibleItem + 5) {
                    if (mListener != null) {
                        mListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mMeteoriteLandingList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meteorite_list_item, parent, false);

            return new MeteoriteItemViewHolder(itemView);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meteorite_list_item_loading, parent, false);

            return new LoadingViewHolder(itemView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MeteoriteItemViewHolder) {
            ((MeteoriteItemViewHolder) holder).bind(mMeteoriteLandingList.get(position), mListener);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).mProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMeteoriteLandingList == null ? 0 : mMeteoriteLandingList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public static class MeteoriteItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView)
        TextView mTextView;

        MeteoriteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final MeteoriteLanding item, final onMeteoriteListAdapterEvents listener) {
            mTextView.setText(String.valueOf(item.getMass()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar mProgressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
