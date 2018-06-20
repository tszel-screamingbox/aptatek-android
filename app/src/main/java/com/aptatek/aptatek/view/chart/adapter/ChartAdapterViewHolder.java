package com.aptatek.aptatek.view.chart.adapter;

import android.content.Context;
import android.view.View;

import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import butterknife.ButterKnife;


public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> {

    private OnItemClickedListener onItemClickedListener;

    ChartAdapterViewHolder(final View view, final Context context) {
        super(view, context);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(final ChartVM data) {

    }

    void setOnItemClickedListener(final OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }


    public interface OnItemClickedListener {
        void onItemClicked(final ChartVM chartVM);
    }
}
