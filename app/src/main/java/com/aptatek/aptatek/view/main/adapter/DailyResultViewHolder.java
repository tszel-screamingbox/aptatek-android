package com.aptatek.aptatek.view.main.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.util.ChartUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyResultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewMeasureInfo)
    TextView textViewMeasureInfo;

    @BindView(R.id.textViewMeasureTime)
    TextView textViewMeasureTime;

    DailyResultViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final DailyResultAdapterItem item) {
        textViewMeasureInfo.setText(item.getMeasureInformationText());
        textViewMeasureInfo.setBackgroundResource(ChartUtils.smallBubbleBackground(item.getState()));
        //20% background opacity based on Figma..
        textViewMeasureInfo.getBackground().setAlpha(51);
        textViewMeasureInfo.setTextColor(ContextCompat.getColor(itemView.getContext(), ChartUtils.stateColor(item.getState())));
        textViewMeasureTime.setTextColor(ContextCompat.getColor(itemView.getContext(), ChartUtils.stateColor(item.getState())));

        final Date dt = new Date(item.getTimestamp());
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        final String time1 = sdf.format(dt);

        textViewMeasureTime.setText(time1);
    }
}
