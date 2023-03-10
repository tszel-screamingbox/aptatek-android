package com.aptatek.pkulab.view.main.home.adapter.chart;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.util.animation.AnimationHelper;
import com.aptatek.pkulab.view.base.list.viewholder.BaseViewHolder;
import com.aptatek.pkulab.view.main.home.adapter.daily.DailyChartFormatter;
import com.aptatek.pkulab.widget.BubbleTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> {

    @BindView(R.id.bubble)
    BubbleTextView bubbleText;

    @BindView(R.id.badge)
    TextView badgeTextView;

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
        bubbleText.setOnClickListener(v -> onItemClickedListener.onItemClicked(data));
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
        animationHelper.animateConstraintWidthAndHeigth(bubbleText, () ->
                resetBubble(currentData), 0.5f);
    }

    private void showDetails(final ChartVM currentData) {
        animationHelper.animateConstraintWidthAndHeigth(bubbleText, () -> {
            final BubbleTextView.BubbleTextConfiguration.Builder configBuilder = BubbleTextView.BubbleTextConfiguration.builder();

            configBuilder.setPrimaryText(dailyChartFormatter.getBubbleValue(currentData.getHighestPkuLevel()))
                    .setSecondaryText(context.getString(currentData.getState()))
                    .setTextColor(ContextCompat.getColor(context, currentData.getColorRes()))
                    .setFillColor(ContextCompat.getColor(context, R.color.applicationWhite))
                    .setCircleColor(ContextCompat.getColor(context, currentData.getColorRes()))
                    .setCircleWidth((int) context.getResources().getDimension(R.dimen.graph_bubble_stroke_big));

            bubbleText.setConfiguration(configBuilder.build());

            if (currentData.getNumberOfMeasures() > 1) {
                badgeTextView.setTextColor(ContextCompat.getColor(context, currentData.getColorRes()));
                badgeTextView.setText(String.valueOf(currentData.getNumberOfMeasures()));
                badgeTextView.setVisibility(View.VISIBLE);
            }

        }, 1f);

    }

    private void resetBubble(final ChartVM currentData) {
        badgeTextView.setVisibility(View.GONE);
        final @ColorInt int color = ContextCompat.getColor(context, currentData.getColorRes());
        final BubbleTextView.BubbleTextConfiguration config = BubbleTextView.BubbleTextConfiguration.builder()
                .setPrimaryText(dailyChartFormatter.formatDailyDate(currentData.getDate().getTime()))
                .setTextColor(context.getResources().getColor(R.color.applicationWhite))
                .setFillColor(color)
                .setCircleColor(color)
                .setCircleWidth(0)
                .build();
        bubbleText.setConfiguration(config);
    }

    public interface OnItemClickedListener {
        void onItemClicked(ChartVM chartVM);
    }
}
