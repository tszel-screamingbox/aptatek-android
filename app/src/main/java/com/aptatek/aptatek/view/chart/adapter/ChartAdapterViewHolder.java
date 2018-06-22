package com.aptatek.aptatek.view.chart.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> {

    @BindView(R.id.infoText)
    TextView infoTextView;

    @BindView(R.id.bubbleLayout)
    RelativeLayout itemLayout;

    private OnItemClickedListener onItemClickedListener;

    ChartAdapterViewHolder(final View view, final Context context) {
        super(view, context);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(final ChartVM data) {
        infoTextView.setText(data.getDate());
        infoTextView.setY(getCurrentY(data.getBubbleYAxis()));
    }

    private float getCurrentY(float relativeValue) {
        final float totalHeight = itemLayout.getLayoutParams().height - infoTextView.getLayoutParams().height;
        return relativeValue * totalHeight;
    }

    void setOnItemClickedListener(final OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }


    public interface OnItemClickedListener {
        void onItemClicked(final ChartVM chartVM);
    }
}
