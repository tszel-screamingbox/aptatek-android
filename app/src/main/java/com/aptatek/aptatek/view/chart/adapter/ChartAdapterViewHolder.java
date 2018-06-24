package com.aptatek.aptatek.view.chart.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.ChartVM;
import com.aptatek.aptatek.view.base.list.viewholder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChartAdapterViewHolder extends BaseViewHolder<ChartVM> implements TextureView.SurfaceTextureListener {

    private static final float STROKE_WIDTH = 10f;

    @BindView(R.id.infoText)
    TextView infoTextView;

    @BindView(R.id.bubbleLayout)
    RelativeLayout itemLayout;

    @BindView(R.id.textureView)
    TextureView itemTextureView;

    private OnItemClickedListener onItemClickedListener;
    private final Context context;

    private final float viewWidth;
    private final float viewHeight;
    private final float bubbleWidth;
    private final float bubbleHeight;
    private final float middleX;
    private final float bubbleX;
    private float bubbleY;

    private ChartVM chartVM;

    ChartAdapterViewHolder(final View view, final Context context) {
        super(view, context);
        this.context = context;
        ButterKnife.bind(this, view);

        viewWidth = itemLayout.getLayoutParams().width;
        viewHeight = itemLayout.getLayoutParams().height;
        bubbleHeight = infoTextView.getLayoutParams().height;
        bubbleWidth = infoTextView.getLayoutParams().width;
        middleX = viewWidth / 2;
        bubbleX = middleX - bubbleWidth / 2;
    }

    @Override
    public void bind(final ChartVM data) {
        chartVM = data;
        itemTextureView.setSurfaceTextureListener(this);

        bubbleY = (viewHeight - bubbleHeight) * chartVM.getBubbleYAxis();

        infoTextView.setText(data.getDate());
        infoTextView.setY(bubbleY);
        infoTextView.setX(bubbleX);
        infoTextView.setOnClickListener(v -> onItemClickedListener.onItemClicked(data));

        if (data.getMaxPhenylalanineLevel() < 0) {
            infoTextView.setBackground(context.getResources().getDrawable(R.drawable.bubble_empty));
        }
    }



    void setOnItemClickedListener(final OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void hideDetails() {
        infoTextView.animate().scaleX(0.9f).scaleY(0.9f)
                .setDuration(200)
                .start();
    }

    public void showDetails() {
        infoTextView.animate().scaleX(2f).scaleY(2f)
                .setDuration(200)
                .start();
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
        linePaint.setPathEffect(new DashPathEffect(new float[]{30, 15, 30, 15}, 0));

        if (chartVM.getStartLineYAxis() >= 0) {
            canvas.drawLine(0, chartVM.getStartLineYAxis(), middleX, middleY, linePaint);
        }

        if (chartVM.getEndLineYAxis() >= 0) {
            canvas.drawLine(middleX, middleY, viewWidth, chartVM.getEndLineYAxis(), linePaint);
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
