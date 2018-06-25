package com.aptatek.aptatek.view.chart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.device.animation.AnimationHelper;
import com.aptatek.aptatek.injection.qualifier.ApplicationContext;
import com.aptatek.aptatek.view.base.list.adapter.BaseAdapter;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import javax.inject.Inject;


public class ChartAdapter extends BaseAdapter<ChartVM> {

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
        ((ChartAdapterViewHolder) holder).setOnItemClickedListener(onItemClickedListener);
    }

    public void setOnItemClickListener(final ChartAdapterViewHolder.OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
