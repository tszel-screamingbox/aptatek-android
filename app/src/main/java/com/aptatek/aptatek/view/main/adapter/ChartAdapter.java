package com.aptatek.aptatek.view.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.util.animation.AnimationHelper;
import com.aptatek.aptatek.view.base.list.adapter.BaseAdapter;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import javax.inject.Inject;


public class ChartAdapter extends BaseAdapter<ChartVM> {

    @Nullable
    private ChartAdapterViewHolder.OnItemClickedListener onItemClickedListener;

    @Inject
    AnimationHelper animationHelper;

    @Inject
    ChartAdapter(@ApplicationContext final Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ChartAdapterViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        final ChartAdapterViewHolder chartAdapterViewHolder = new ChartAdapterViewHolder(view, context, animationHelper);
        prepareItemOnClick(chartAdapterViewHolder);
        return chartAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<ChartVM> holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (onItemClickedListener != null) {
            ((ChartAdapterViewHolder) holder).setOnItemClickedListener(onItemClickedListener);
        }
    }

    public void setOnItemClickListener(@NonNull final ChartAdapterViewHolder.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
