package com.aptatek.pkulab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.aptatek.pkulab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RangeInfoRowView extends ConstraintLayout {

    @BindView(R.id.rangeinfo_range_value)
    TextView tvRangeValue;
    @BindView(R.id.rangeinfo_range_name)
    TextView tvRangeName;

    public RangeInfoRowView(final Context context) {
        this(context, null, 0);
    }

    public RangeInfoRowView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeInfoRowView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_rangeinfo_range, this);

        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RangeInfoRowView, 0, 0);

        try {
            final int color = array.getColor(R.styleable.RangeInfoRowView_backgroundColor, -1);
            if (color != -1) {
                final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.rangeinfo_row_background);
                drawable.setColorFilter(color, PorterDuff.Mode.OVERLAY);
                setBackground(drawable);
            }
        } finally {
            array.recycle();
        }

        ButterKnife.bind(this);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        final int heightSpec = MeasureSpec.makeMeasureSpec(getResources().getDimensionPixelSize(R.dimen.general_button_height_big), MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightSpec);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    public void setRange(final String rangeValue, final String rangeName) {
        tvRangeValue.setText(rangeValue);
        tvRangeName.setText(rangeName);
        invalidate();
    }

}
