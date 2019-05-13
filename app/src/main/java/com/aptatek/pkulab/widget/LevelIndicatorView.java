package com.aptatek.pkulab.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.aptatek.pkulab.R;

public class LevelIndicatorView extends View {

    public LevelIndicatorView(final Context context) {
        this(context, null);
    }

    public LevelIndicatorView(final Context context, final @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelIndicatorView(final Context context, final @Nullable AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LevelIndicatorView(final Context context, final @Nullable AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(final Context context, final @Nullable AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LevelIndicatorView, defStyleAttr, defStyleRes);
        try {
            initBackground(typedArray.getColor(R.styleable.LevelIndicatorView_indicatorColor, ContextCompat.getColor(context, android.R.color.transparent)));
        } finally {
            typedArray.recycle();
        }
    }

    private void initBackground(final @ColorInt int color) {
        final ShapeDrawable background = new ShapeDrawable(new OvalShape());
        final Paint paint = background.getPaint();
        paint.setColor(color);
        setBackground(background);
    }
}
