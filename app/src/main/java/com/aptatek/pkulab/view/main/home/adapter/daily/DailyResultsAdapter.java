package com.aptatek.pkulab.view.main.home.adapter.daily;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.view.base.BaseAdapter;

import javax.inject.Inject;

public class DailyResultsAdapter extends BaseAdapter<DailyResultViewHolder, DailyResultAdapterItem> {

    @Inject
    public DailyResultsAdapter() {
    }

    @Override
    protected DailyResultViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new DailyResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_result_item, parent, false));
    }

    @Override
    protected void bindData(@NonNull final DailyResultViewHolder holder, final int position) {
        holder.bind(data.getCurrentList().get(holder.getAdapterPosition()));
    }
}
