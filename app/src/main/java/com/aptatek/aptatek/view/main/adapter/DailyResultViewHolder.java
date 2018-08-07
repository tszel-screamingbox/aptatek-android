package com.aptatek.aptatek.view.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.domain.interactor.ResourceInteractor;

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

    private final ResourceInteractor resourceInteractor;

    DailyResultViewHolder(final View itemView,
                          final ResourceInteractor resourceInteractor) {
        super(itemView);
        this.resourceInteractor = resourceInteractor;
        ButterKnife.bind(this, itemView);
    }

    public void bind(final DailyResultAdapterItem item) {
        textViewMeasureInfo.setText(item.getMeasureInformationText());
        textViewMeasureInfo.setBackgroundResource(item.getBackgroundRes());
        //20% background opacity based on Figma..
        textViewMeasureInfo.getBackground().setAlpha(51);
        final int color = resourceInteractor.getColorResource(item.getColorRes());
        textViewMeasureInfo.setTextColor(color);
        textViewMeasureTime.setTextColor(color);

        textViewMeasureInfo.setPadding(
                (int) resourceInteractor.getDimension(R.dimen.general_distance_small),
                (int) resourceInteractor.getDimension(R.dimen.general_distance_mini),
                (int) resourceInteractor.getDimension(R.dimen.general_distance_small),
                (int) resourceInteractor.getDimension(R.dimen.general_distance_mini));


        // TODO extract to a formatter and use string resource for the format pattern
        final Date dt = new Date(item.getTimestamp());
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        final String time1 = sdf.format(dt);

        textViewMeasureTime.setText(time1);
    }
}
