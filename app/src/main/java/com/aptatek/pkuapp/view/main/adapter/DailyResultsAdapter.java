package com.aptatek.pkuapp.view.main.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.interactor.ResourceInteractor;
import com.aptatek.pkuapp.view.base.BaseAdapter;

import javax.inject.Inject;

public class DailyResultsAdapter extends BaseAdapter<DailyResultViewHolder, DailyResultAdapterItem> {

    private final ResourceInteractor resourceInteractor;

    @Inject
    public DailyResultsAdapter(final ResourceInteractor resourceInteractor) {
        this.resourceInteractor = resourceInteractor;
    }

    @Override
    protected DailyResultViewHolder setViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new DailyResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_daily_result_item, parent, false), resourceInteractor);
    }

    @Override
    protected void bindData(@NonNull final DailyResultViewHolder holder, final int position) {
        holder.bind(data.getCurrentList().get(holder.getAdapterPosition()));
    }
}
