package com.aptatek.pkulab.view.main.home.adapter.daily;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.widget.RangeInfoRowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyResultViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.rangeInfoLayout)
    RangeInfoRowView rangeInfoRowView;

    DailyResultViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final DailyResultAdapterItem item) {
        // TODO extract to a formatter and use string resource for the format pattern
        final Date dt = new Date(item.getTimestamp());
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        final String time1 = sdf.format(dt);

        rangeInfoRowView.setBackgroundTint(item.getColorRes());
        rangeInfoRowView.setRange(item.getMeasureInformationText().toString(), time1);
    }
}
