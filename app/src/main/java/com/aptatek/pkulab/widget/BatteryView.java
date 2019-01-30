package com.aptatek.pkulab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.pkulab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BatteryView extends ConstraintLayout {

    private static final int BATTERY_DEFAULT = 100;
    private static final int BATTERY_LOW = 10;
    private static final int BATTERY_NORMAL = 35;

    private final Context context;

    @BindView(R.id.batteryImage)
    ImageView batteryImageView;
    @BindView(R.id.batteryLevel)
    TextView levelTextView;

    public BatteryView(final Context context) {
        this(context, null, 0);
    }

    public BatteryView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        LayoutInflater.from(getContext()).inflate(R.layout.layout_battery_view, this);

        ButterKnife.bind(this);

        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BatteryView, 0, 0);

        try {
            final int percentage = array.getInteger(R.styleable.BatteryView_percentage, BATTERY_DEFAULT);
            showBattery(percentage);
        } finally {
            array.recycle();
        }
    }

    public void setBatteryLevel(final int batteryLevel) {
        showBattery(batteryLevel);
        invalidate();
    }

    private void showBattery(final int level) {

        levelTextView.setText(context.getString(R.string.widget_battery_percentage, level));

        if (level <= BATTERY_LOW) {
            batteryImageView.setImageResource(R.drawable.ic_battery_low);
            levelTextView.setTextColor(ContextCompat.getColor(context, R.color.batteryLevelLow));
        } else if (level <= BATTERY_NORMAL) {
            batteryImageView.setImageResource(R.drawable.ic_battery_normal);
            levelTextView.setTextColor(ContextCompat.getColor(context, R.color.batteryLevelNormal));
        } else {
            batteryImageView.setImageResource(R.drawable.ic_battery_high);
            levelTextView.setTextColor(ContextCompat.getColor(context, R.color.batteryLevelHigh));
        }
    }
}
