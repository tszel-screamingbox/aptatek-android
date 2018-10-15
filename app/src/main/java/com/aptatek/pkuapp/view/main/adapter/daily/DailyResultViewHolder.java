package com.aptatek.pkuapp.view.main.adapter.daily;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;

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

    @BindView(R.id.containerConstraintLayout)
    ConstraintLayout container;

    DailyResultViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final DailyResultAdapterItem item) {
        textViewMeasureInfo.setText(item.getMeasureInformationText());
        container.setBackgroundResource(item.getBackgroundRes());

        // TODO extract to a formatter and use string resource for the format pattern
        final Date dt = new Date(item.getTimestamp());
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        final String time1 = sdf.format(dt);

        textViewMeasureTime.setText(time1);
    }
}
