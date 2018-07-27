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
import com.aptatek.aptatek.util.ChartUtils;
import com.aptatek.aptatek.util.animation.AnimationHelper;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> implements TextureView.SurfaceTextureListener {

    private static final float STROKE_WIDTH = 10f;
    private static final float INTERVAL = 5;


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
        // need a margin, because of the zooming animation
        marginY = bubbleHeight * 0.5f;
        viewHeight = itemLayout.getLayoutParams().height - bubbleHeight * 2f;
        // get the middle of the cell
        middleX = viewWidth / 2;
        // calculate the X-coordinate of the bubble, taking care of the bubble width
        bubbleX = middleX - infoTextView.getLayoutParams().width / 2;
    }

    @Override
    public void bind(final ChartVM data) {
        itemTextureView.setSurfaceTextureListener(this);
        currentData = data;
        // set coordinates for the bubble
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
        currentData.setZoomed(false);
    }

    public void showDetails() {
        currentData.setZoomed(true);
        animationHelper.zoomIn(bubbleContainerLayout, () -> {
            if (currentData.isEmpty()) {
                infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_empty));
            } else {
                final ChartUtils.State state = ChartUtils.getState(currentData.getMaxPhenylalanineLevel());
                infoTextView.setBackground(context.getResources().getDrawable(ChartUtils.bigBubbleBackground(state)));
                infoTextView.setTextColor(context.getResources().getColor(ChartUtils.stateColor(state)));
                infoTextView.setText(currentData.getDetails());
                if (currentData.getNumberOfMeasures() > 1) {
                    badgeTextView.setBackground(context.getResources().getDrawable(ChartUtils.smallBubbleBackground(state)));
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
            final ChartUtils.State state = ChartUtils.getState(currentData.getMaxPhenylalanineLevel());
            infoTextView.setBackground(context.getResources().getDrawable(ChartUtils.smallBubbleBackground(state)));
        }
    }

    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, final int width, final int height) {
        // canvas for drawing
        final Canvas canvas = itemTextureView.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(context.getResources().getColor(R.color.chartBackrgroundBlue));
        // calculate the start-line ending Y-height, and the end-line start Y-height
        final float middleY = bubbleY + bubbleHeight / 2;
        // create Paint for the start-line and end-line
        final Paint linePaint = new Paint();
        linePaint.setColor(context.getResources().getColor(R.color.applicationBlue));
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(STROKE_WIDTH);
        linePaint.setPathEffect(new DashPathEffect(new float[]{INTERVAL, INTERVAL, INTERVAL, INTERVAL}, 0));

        // calculate offset, it's necessary: the lines Y-coordinates not equals with the bubble Y-height
        final float offSet = (bubbleHeight / 2) / viewHeight;
        // if the current item is not the first
        if (currentData.getStartLineYAxis() >= 0) {
            // calculate start-line starting Y-height
            final float startY = (currentData.getStartLineYAxis() + offSet) * viewHeight + marginY;
            // draw the start-line: from the left side of the item cell to the middle
            canvas.drawLine(0, startY, middleX, middleY, linePaint);
        }
        // if the current item is not the last
        if (currentData.getEndLineYAxis() >= 0) {
            // calculate end-line ending Y-height
            final float stopY = (currentData.getEndLineYAxis() + offSet) * viewHeight + marginY;
            // draw the end-line: from the middle of the cell to the end of its
            canvas.drawLine(middleX, middleY, viewWidth, stopY, linePaint);
        }
        itemTextureView.unlockCanvasAndPost(canvas);
    }

    @Override
    public void onSurfaceTextureSizeChanged(final SurfaceTexture surface, final int width, final int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(final SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(final SurfaceTexture surface) {

    }

    public interface OnItemClickedListener {
        void onItemClicked(ChartVM chartVM);
    }
}
