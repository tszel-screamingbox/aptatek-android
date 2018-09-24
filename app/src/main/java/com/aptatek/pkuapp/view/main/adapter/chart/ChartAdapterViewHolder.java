package com.aptatek.pkuapp.view.main.adapter.chart;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptatek.pkuapp.R;
import com.aptatek.pkuapp.domain.model.PkuLevel;
import com.aptatek.pkuapp.util.animation.AnimationHelper;
import com.aptatek.pkuapp.view.base.list.viewholder.BaseViewHolder;
import com.aptatek.pkuapp.view.main.adapter.daily.DailyChartFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> {

    @BindView(R.id.infoText)
    TextView infoTextView;

    @BindView(R.id.badge)
    TextView badgeTextView;

    @BindView(R.id.bubbleContainer)
    RelativeLayout bubbleContainerLayout;

    private OnItemClickedListener onItemClickedListener;
    private final AnimationHelper animationHelper;
    private final DailyChartFormatter dailyChartFormatter;

    ChartAdapterViewHolder(final View view,
                           final Context context,
                           final AnimationHelper animationHelper,
                           final DailyChartFormatter dailyChartFormatter) {
        super(view, context);
        this.animationHelper = animationHelper;
        this.dailyChartFormatter = dailyChartFormatter;
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(final ChartVM data) {
        infoTextView.setOnClickListener(v -> onItemClickedListener.onItemClicked(data));
        resetBubble(data);

        if (data.isZoomed()) {
            showDetails(data);
        } else {
            hideDetails(data);
        }
    }

    void setOnItemClickedListener(final OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    private void hideDetails(final ChartVM currentData) {
        animationHelper.zoomOut(bubbleContainerLayout, () -> resetBubble(currentData));
    }

    private void showDetails(final ChartVM currentData) {
        infoTextView.getLayoutParams().width = calculateWidth(infoTextView);
        animationHelper.zoomIn(bubbleContainerLayout, () -> {
            final PkuLevel highestMeasure = currentData.getHighestPkuLevel();
            if (highestMeasure == null) {
                bubbleContainerLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bubble_empty));
            } else {
                infoTextView.setTextColor(ContextCompat.getColor(context, currentData.getColorRes()));
                bubbleContainerLayout.setBackground(ContextCompat.getDrawable(context, currentData.getExpandedBackgroundRes()));

                final CharSequence infoText = dailyChartFormatter.getBubbleText(highestMeasure, currentData.getState());
                infoTextView.setText(infoText);
                if (currentData.getNumberOfMeasures() > 1) {
                    badgeTextView.setTextColor(ContextCompat.getColor(context, currentData.getColorRes()));
                    badgeTextView.setText(String.valueOf(currentData.getNumberOfMeasures()));
                    badgeTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private int calculateWidth(final TextView textView) {
        final float margin = context.getResources().getDimension(R.dimen.graph_bubble_margin);
        final float diameter = (bubbleContainerLayout.getLayoutParams().width - 2 * margin) * 0.5f;
        final float distance = textView.getLayoutParams().height * 0.5f;
        return (int) (2 * Math.sqrt(Math.pow(diameter, 2) - Math.pow(distance, 2)));
    }

    private void resetBubble(final ChartVM currentData) {
        badgeTextView.setVisibility(View.GONE);
        infoTextView.setText(dailyChartFormatter.formatDailyDate(currentData.getDate().getTime()));
        infoTextView.setTextColor(context.getResources().getColor(R.color.applicationWhite));
        if (currentData.getHighestPkuLevel() == null) {
            bubbleContainerLayout.setBackground(context.getResources().getDrawable(R.drawable.bubble_empty));
        } else {
            bubbleContainerLayout.setBackground(context.getResources().getDrawable(currentData.getCollapsedBackgroundRes()));
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(ChartVM chartVM);
    }
}
