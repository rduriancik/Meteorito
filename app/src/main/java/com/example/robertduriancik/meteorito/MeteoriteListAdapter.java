package com.example.robertduriancik.meteorito;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeteoriteListAdapter extends RecyclerView.Adapter<MeteoriteListAdapter.MeteoriteItemHolder> {

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

    private final List<String> mStringList;
    private final OnItemClickListener mListener;

    public MeteoriteListAdapter(List<String> stringList, OnItemClickListener listener) {
        this.mStringList = stringList;
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
        holder.bind(mStringList.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    public static class MeteoriteItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textView)
        TextView mTextView;

        public MeteoriteItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final String item, final OnItemClickListener listener) {
            mTextView.setText(item);
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
