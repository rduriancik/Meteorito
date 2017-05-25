package com.example.robertduriancik.meteorito.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeteoriteListAdapter extends RecyclerView.Adapter<MeteoriteListAdapter.MeteoriteItemViewHolder> {

    public interface onMeteoriteListAdapterInteraction {
        void onItemClick(MeteoriteLanding meteoriteLanding);
    }

    private final List<MeteoriteLanding> mMeteoriteLandingList;
    private final onMeteoriteListAdapterInteraction mListener;

    public MeteoriteListAdapter(List<MeteoriteLanding> meteoriteLandingList, onMeteoriteListAdapterInteraction listener) {
        this.mMeteoriteLandingList = meteoriteLandingList;
        this.mListener = listener;
    }

    @Override
    public MeteoriteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meteorite_list_item, parent, false);

            return new MeteoriteItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeteoriteItemViewHolder holder, int position) {
            ((MeteoriteItemViewHolder) holder).bind(mMeteoriteLandingList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mMeteoriteLandingList == null ? 0 : mMeteoriteLandingList.size();
    }

    public static class MeteoriteItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView)
        TextView mTextView;

        MeteoriteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final MeteoriteLanding item, final onMeteoriteListAdapterInteraction listener) {
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

}
