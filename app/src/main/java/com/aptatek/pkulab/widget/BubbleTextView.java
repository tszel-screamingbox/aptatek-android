package com.aptatek.pkulab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.google.auto.value.AutoValue;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class BubbleTextView extends View {

    private static final boolean DRAW_HELPERS = false;
    private static final float DEFAULT_PADDING = 0.8f;
    private static final float TEXT_SIZE_DECREASE_RATIO = 0.1f;
    private static final float MIN_TEXT_SIZE_DECREASE = 10f;
    private static final double PRIMARY_TO_SECONDARY_RATIO = 0.7d;

    private BubbleTextConfiguration configuration;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Typeface primaryTypeFace;
    private final Typeface secondaryTypeFace;
    private final Rect boundingRect = new Rect();
    private final TextPaint textPaint = new TextPaint(paint);
    private final Map<CachedTextSizeKey, Float> cachedTextSizes = new HashMap<>();

    public BubbleTextView(@NonNull final Context context) {
        this(context, null);
    }

    public BubbleTextView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleTextView(@NonNull final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);

        primaryTypeFace = ResourcesCompat.getFont(context, R.font.nunito_black);
        secondaryTypeFace = primaryTypeFace;
    }

    private void init(final Context context, final @Nullable AttributeSet attrs, final int defStyleAttr) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BubbleTextView, defStyleAttr, 0);
        try {
            final int circleColor = typedArray.getColor(R.styleable.BubbleTextView_circleColor, Color.TRANSPARENT);
            final int circleWidth = Math.round(typedArray.getDimension(R.styleable.BubbleTextView_circleWidth, 0f));
            final int fillColor = typedArray.getColor(R.styleable.BubbleTextView_fillColor, Color.TRANSPARENT);
            final int textColor = typedArray.getColor(R.styleable.BubbleTextView_textColor, Color.BLACK);
            final String primaryText = typedArray.getString(R.styleable.BubbleTextView_textPrimary);
            final String secondaryText = typedArray.getString(R.styleable.BubbleTextView_textSecondary);

            configuration = BubbleTextConfiguration.builder()
                    .setCircleColor(circleColor)
                    .setCircleWidth(circleWidth)
                    .setFillColor(fillColor)
                    .setTextColor(textColor)
                    .setPrimaryText(primaryText)
                    .setSecondaryText(secondaryText)
                    .build();
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int size;
        if (widthMode == MeasureSpec.EXACTLY && widthSize > 0) {
            size = widthSize;
        } else if (heightMode == MeasureSpec.EXACTLY && heightSize > 0) {
            size = heightSize;
        } else {
            size = widthSize < heightSize ? widthSize : heightSize;
        }

        final int finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);

        super.onMeasure(finalMeasureSpec, finalMeasureSpec);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        final int size = Math.min(canvas.getWidth(), canvas.getHeight());
        final float middlePoint = size / 2f;
        final int diameter = size - configuration.getCircleWidth();
        final float radius = (float) Math.floor(diameter / 2f);
        final float innerCircleRadius = radius - (configuration.getCircleWidth() == 0 ? 0f : configuration.getCircleWidth() / 2f);

        // draw circle
        if (configuration.getCircleWidth() > 0) {
            paint.setStrokeWidth(configuration.getCircleWidth());
            paint.setColor(configuration.getCircleColor());
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(middlePoint, middlePoint, radius, paint);
        }

        // fill inside
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0f);
        paint.setColor(configuration.getFillColor());
        canvas.drawCircle(middlePoint, middlePoint, innerCircleRadius, paint);

        final String primaryText = configuration.getPrimaryText();
        if (!TextUtils.isEmpty(configuration.getSecondaryText()) && !TextUtils.isEmpty(primaryText)) {
            // 2 rows
            final Rect rect = calculateLargestSquareInsideCircle((int) middlePoint, (int) middlePoint, innerCircleRadius);
            final Rect primaryTextBounds = calculatePrimaryTextBounds(rect);

            paint.setTypeface(primaryTypeFace);
            paint.setColor(configuration.getTextColor());
            textPaint.set(paint);

            textPaint.setTextSize(primaryTextBounds.width());
            getTextBounds(primaryText, textPaint);

            while (!textFitsInsideBounds(boundingRect, primaryTextBounds)) {
                Timber.d("Need to adjust primary text size: widthToFit [%d], heightToFit [%d], textWidth [%d], textHeight = [%d], textSize [%.0f], text [%s]", primaryTextBounds.width(), primaryTextBounds.height(), boundingRect.width(), boundingRect.height(), textPaint.getTextSize(), primaryText);

                final float textSizeDecreasedPercentage = textPaint.getTextSize() * TEXT_SIZE_DECREASE_RATIO;
                final float newTextSize = textPaint.getTextSize() - Math.max(textSizeDecreasedPercentage, MIN_TEXT_SIZE_DECREASE);
                textPaint.setTextSize(newTextSize);
                getTextBounds(primaryText, textPaint);
            }

            canvas.drawText(primaryText, (size - boundingRect.width()) / 2f, primaryTextBounds.bottom - ((primaryTextBounds.height() - boundingRect.height())  / 2), textPaint);

            final Rect secondaryTextBounds = calculateSecondaryTextBounds(rect);
            paint.setTypeface(secondaryTypeFace);
            textPaint.set(paint);

            final String secondaryText = configuration.getSecondaryText();
            textPaint.setTextSize(secondaryTextBounds.width());
            getTextBounds(secondaryText, textPaint);

            while (!textFitsInsideBounds(boundingRect, secondaryTextBounds)) {
                Timber.d("Need to adjust secondary text size: widthToFit [%d], heightToFit [%d], textWidth [%d], textHeight = [%d], textSize [%.0f], text [%s]", secondaryTextBounds.width(), secondaryTextBounds.height(), boundingRect.width(), boundingRect.height(), textPaint.getTextSize(), secondaryText);

                final float textSizeDecreasedPercentage = textPaint.getTextSize() * TEXT_SIZE_DECREASE_RATIO;
                final float newTextSize = textPaint.getTextSize() - Math.max(textSizeDecreasedPercentage, MIN_TEXT_SIZE_DECREASE);
                textPaint.setTextSize(newTextSize);
                getTextBounds(secondaryText, textPaint);
            }

            canvas.drawText(secondaryText, (size - boundingRect.width()) / 2f, secondaryTextBounds.top + boundingRect.height(), textPaint);

            if (BuildConfig.DEBUG && DRAW_HELPERS) {
                paint.setColor(Color.argb(30, 255, 0, 0));
                canvas.drawRect(primaryTextBounds, paint);
            }

            if (BuildConfig.DEBUG && DRAW_HELPERS) {
                paint.setColor(Color.argb(30, 0, 0, 255));
                canvas.drawRect(secondaryTextBounds, paint);
            }

        } else if (!TextUtils.isEmpty(primaryText)) {
            // only primary text
            paint.setTypeface(primaryTypeFace);
            paint.setColor(configuration.getTextColor());
            textPaint.set(paint);
            calculateTextSizeToFitCircle(primaryText, diameter);
            canvas.drawText(primaryText, (size - boundingRect.width()) / 2f, middlePoint + (boundingRect.height() / 2f), textPaint);
        }

        canvas.restore();
    }

    private boolean textFitsInsideBounds(@NonNull final Rect current, @NonNull final Rect maxBounds) {
        return current.width() <= maxBounds.width() && current.height() <= maxBounds.height();
    }

    private void getTextBounds(@NonNull final String text, @NonNull final TextPaint textPaint) {
        textPaint.getTextBounds(text, 0, text.length(), boundingRect);
    }

    private void calculateTextSizeToFitCircle(@NonNull final String text, final int diameter) {
        final int widthToFit = (int) (diameter * DEFAULT_PADDING);
        final CachedTextSizeKey cachedTextSizeKey = CachedTextSizeKey.create(diameter, text);

        final Float cachedSize = cachedTextSizes.get(cachedTextSizeKey);
        if (cachedSize != null) {
            Timber.d("We have a previously calculated primary text size for diameter [%d] and text [%s] -> text size [%.0f]", diameter, text, cachedSize);
            textPaint.setTextSize(cachedSize);
            getTextBounds(text, textPaint);

            if (textFitsInsideCircle(boundingRect, widthToFit)) {
                Timber.d("Using previously calculated primary text size!");

                return;
            }
        }

        textPaint.setTextSize(diameter);
        getTextBounds(text, textPaint);

        while (!textFitsInsideCircle(boundingRect, widthToFit)) {
            Timber.d("Need to adjust text size: widthToFit [%d], textWidth [%d], textSize [%.0f], text [%s]", widthToFit, boundingRect.width(), textPaint.getTextSize(), text);

            final float textSizeDecreasedPercentage = textPaint.getTextSize() * TEXT_SIZE_DECREASE_RATIO;
            final float newTextSize = textPaint.getTextSize() - Math.max(textSizeDecreasedPercentage, MIN_TEXT_SIZE_DECREASE);
            textPaint.setTextSize(newTextSize);
            getTextBounds(text, textPaint);
        }

        cachedTextSizes.put(cachedTextSizeKey, textPaint.getTextSize());
    }

    private Rect calculateLargestSquareInsideCircle(final int circleX, final int circleY, final float radius) {
        final Rect square = new Rect();
        final double size = Math.sqrt(Math.pow(radius * 2, 2d) / 2d);
        final int halfSize = (int) (size / 2d);
        square.set(circleX - halfSize, circleY - halfSize, circleX + halfSize, circleY + halfSize);

        return square;
    }

    private Rect calculatePrimaryTextBounds(final Rect largestSquare) {
        final Rect primaryBounds = new Rect();
        final double height = largestSquare.height() * PRIMARY_TO_SECONDARY_RATIO;
        primaryBounds.set(largestSquare.left, largestSquare.top, largestSquare.right, largestSquare.top + (int) height);

        return primaryBounds;
    }

    private Rect calculateSecondaryTextBounds(final Rect largestSquare) {
        final Rect secondaryBounds = new Rect();
        final double height = largestSquare.height() * PRIMARY_TO_SECONDARY_RATIO;
        secondaryBounds.set(largestSquare.left, largestSquare.top + (int) height, largestSquare.right, largestSquare.bottom);

        return secondaryBounds;
    }

    private boolean textFitsInsideCircle(final Rect textBounds, final int widthToFit) {
        final int width = textBounds.width();
        final int height = textBounds.height();
        final int radius = (int) (widthToFit / 2f);

        return width <= widthToFit
                && Math.abs(Math.sqrt(
                Math.pow(radius, 2) // r^2
                        - Math.pow((widthToFit - width) / 2, 2)) // - x^2
        ) * 2 >= height;
    }

    @NonNull
    public BubbleTextConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(@NonNull final BubbleTextConfiguration configuration) {
        this.configuration = configuration;
        invalidate();
    }

    @AutoValue
    public abstract static class BubbleTextConfiguration {
        public abstract @ColorInt
        int getCircleColor();

        public abstract @ColorInt
        int getFillColor();

        public abstract @ColorInt
        int getTextColor();

        public abstract int getCircleWidth();

        public abstract @Nullable
        String getPrimaryText();

        public abstract @Nullable
        String getSecondaryText();

        public abstract Builder toBuilder();

        public static Builder builder() {
            return new AutoValue_BubbleTextView_BubbleTextConfiguration.Builder()
                    .setPrimaryText("");
        }

        @AutoValue.Builder
        public abstract static class Builder {

            public abstract Builder setCircleColor(@ColorInt final int circleColor);

            public abstract Builder setFillColor(@ColorInt final int fillColor);

            public abstract Builder setTextColor(@ColorInt final int textColor);

            public abstract Builder setCircleWidth(final int circleWidth);

            public abstract Builder setPrimaryText(@Nullable final String primaryText);

            public abstract Builder setSecondaryText(@Nullable final String secondaryText);

            public abstract BubbleTextConfiguration build();

        }
    }

    @AutoValue
    static abstract class CachedTextSizeKey {

        public abstract int getDiameter();

        @NonNull
        public abstract String getText();

        public static CachedTextSizeKey create(final int diameter, @NonNull final String text) {
            return new AutoValue_BubbleTextView_CachedTextSizeKey(diameter, text);
        }
    }
}
