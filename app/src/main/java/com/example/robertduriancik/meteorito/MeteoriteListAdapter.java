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

    private List<String> mStringList;

    public MeteoriteListAdapter(List<String> stringList) {
        this.mStringList = stringList;
    }

    @Override
    public MeteoriteItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meteorite_list_item, parent, false);

        return new MeteoriteItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeteoriteItemHolder holder, int position) {
        holder.mTextView.setText(mStringList.get(position));
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
    }
}
