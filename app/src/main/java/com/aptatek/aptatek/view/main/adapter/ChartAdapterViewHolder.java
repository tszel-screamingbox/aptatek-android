package com.aptatek.aptatek.view.main.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.support.constraint.ConstraintLayout;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.util.animation.AnimationHelper;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> implements TextureView.SurfaceTextureListener {

    private static final float STROKE_WIDTH = 10f;


    @BindView(R.id.bubbleLayout)
    ConstraintLayout itemLayout;

    @BindView(R.id.infoText)
    TextView infoTextView;

    @BindView(R.id.textureView)
    TextureView itemTextureView;

    @BindView(R.id.badge)
    TextView badgeTextView;

    @BindView(R.id.bubbleContainer)
    RelativeLayout bubbleContainerLayout;

    private OnItemClickedListener onItemClickedListener;
    private final Context context;
    private final AnimationHelper animationHelper;

    private final float viewWidth;
    private final float viewHeight;
    private final float bubbleHeight;
    private final float middleX;
    private final float bubbleX;
    private final float marginY;

    private float bubbleY;

    private ChartVM currentData;

    ChartAdapterViewHolder(final View view, final Context context, final AnimationHelper animationHelper) {
        super(view, context);
        this.context = context;
        this.animationHelper = animationHelper;
        ButterKnife.bind(this, view);

        bubbleHeight = infoTextView.getLayoutParams().height;
        viewWidth = itemLayout.getLayoutParams().width;
        marginY = bubbleHeight * 0.5f;
        viewHeight = itemLayout.getLayoutParams().height - bubbleHeight * 2f;
        middleX = viewWidth / 2;
        bubbleX = middleX - infoTextView.getLayoutParams().width / 2;
    }

    @Override
    public void bind(final ChartVM data) {
        itemTextureView.setSurfaceTextureListener(this);
        currentData = data;
        bubbleY = viewHeight * currentData.getBubbleYAxis() + marginY;
        bubbleContainerLayout.setY(bubbleY);
        bubbleContainerLayout.setX(bubbleX);
        infoTextView.setOnClickListener(v -> onItemClickedListener.onItemClicked(data));
        resetBubble();
    }


    void setOnItemClickedListener(final OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void hideDetails() {
        animationHelper.zoomOut(bubbleContainerLayout, this::resetBubble);
    }

    public void showDetails() {
        animationHelper.zoomIn(bubbleContainerLayout, () -> {
            if (currentData.isEmpty()) {
                infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_empty));
            } else {
                infoTextView.setTextColor(context.getResources().getColor(R.color.applicationGreen));
                infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_big_detail));
                infoTextView.setText(currentData.getDetails());
                if (currentData.getNumberOfMeasures() > 1) {
                    badgeTextView.setText(String.valueOf(currentData.getNumberOfMeasures()));
                    badgeTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void resetBubble() {
        badgeTextView.setVisibility(View.GONE);
        infoTextView.setText(currentData.getFormattedDate());
        infoTextView.setTextColor(context.getResources().getColor(R.color.applicationWhite));
        if (currentData.isEmpty()) {
            infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_empty));
        } else {
            infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_green));
        }
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
        final Canvas canvas = itemTextureView.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(context.getResources().getColor(R.color.chartBackrgroundBlue));
        final float middleY = bubbleY + bubbleHeight / 2;
        final Paint linePaint = new Paint();
        linePaint.setColor(context.getResources().getColor(R.color.applicationBlue));
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(STROKE_WIDTH);
        linePaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5}, 0));

        final float offSet = (bubbleHeight / 2) / viewHeight;
        if (currentData.getStartLineYAxis() >= 0) {
            final float startY = (currentData.getStartLineYAxis() + offSet) * viewHeight + marginY;
            canvas.drawLine(0, startY, middleX, middleY, linePaint);
        }

        if (currentData.getEndLineYAxis() >= 0) {
            final float stopY = (currentData.getEndLineYAxis() + offSet) * viewHeight + marginY;
            canvas.drawLine(middleX, middleY, viewWidth, stopY, linePaint);
        }
        itemTextureView.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public interface OnItemClickedListener {
        void onItemClicked(final ChartVM chartVM);
    }
}
