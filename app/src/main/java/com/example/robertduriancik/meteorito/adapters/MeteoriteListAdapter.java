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

public class MeteoriteListAdapter extends RecyclerView.Adapter<MeteoriteListAdapter.MeteoriteItemHolder> {

    public interface OnItemClickListener {
        void onItemClick(MeteoriteLanding meteoriteLanding);
    }

    private final List<MeteoriteLanding> mMeteoriteLandingList;
    private final OnItemClickListener mListener;

    public MeteoriteListAdapter(List<MeteoriteLanding> meteoriteLandingList, OnItemClickListener listener) {
        this.mMeteoriteLandingList = meteoriteLandingList;
        this.mListener = listener;
    }

    @Override
    public MeteoriteItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteorite_list_item, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return new MeteoriteItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeteoriteItemHolder holder, int position) {
        holder.bind(mMeteoriteLandingList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mMeteoriteLandingList.size();
    }

    public static class MeteoriteItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView)
        TextView mTextView;

        MeteoriteItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final MeteoriteLanding item, final OnItemClickListener listener) {
            mTextView.setText(item.getName());
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
