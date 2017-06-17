package com.example.robertduriancik.meteorito.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robertduriancik.meteorito.R;
import com.example.robertduriancik.meteorito.models.MeteoriteLanding;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeteoriteListAdapter extends RecyclerView.Adapter<MeteoriteListAdapter.MeteoriteItemViewHolder> {

    public interface OnMeteoriteListAdapterInteractionListener {
        void onItemClick(MeteoriteLanding meteoriteLanding);
    }

    private final List<MeteoriteLanding> mMeteoriteLandingList;
    private OnMeteoriteListAdapterInteractionListener mListener;

    public MeteoriteListAdapter(List<MeteoriteLanding> meteoriteLandingList) {
        this.mMeteoriteLandingList = meteoriteLandingList;
    }

    @Override
    public MeteoriteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteorite_list_item, parent, false);

        return new MeteoriteItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeteoriteItemViewHolder holder, int position) {
        holder.bind(mMeteoriteLandingList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mMeteoriteLandingList == null ? 0 : mMeteoriteLandingList.size();
    }

    public void addOnMeteoriteListAdapterInteractionListener(OnMeteoriteListAdapterInteractionListener listener) {
        this.mListener = listener;
    }

    public MeteoriteLanding getItem(int position) {
        if (mMeteoriteLandingList != null &&
                position >= 0 &&
                position < mMeteoriteLandingList.size()) {
            return mMeteoriteLandingList.get(position);
        }

        return null;
    }

    static class MeteoriteItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_name)
        TextView mName;
        @BindView(R.id.item_class)
        TextView mClass;
        @BindView(R.id.item_mass)
        TextView mMass;
        @BindView(R.id.item_year)
        TextView mYear;

        MeteoriteItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(final MeteoriteLanding item, final OnMeteoriteListAdapterInteractionListener listener) {
            mName.setText(item.getName());
            mClass.setText(item.getRecClass());
            mMass.setText(String.valueOf(item.getMass()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getDate());
            mYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
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
