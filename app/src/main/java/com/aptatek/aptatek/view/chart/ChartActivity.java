package com.aptatek.aptatek.view.chart;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.github.mikephil.charting.charts.LineChart;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartActivity extends BaseActivity<ChartActivityView, ChartActivityPresenter> implements ChartActivityView {


    @Inject
    ChartActivityPresenter presenter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        ButterKnife.bind(this);
    }

    private Bitmap getBitmap(final int drawableRes) {
        final Drawable drawable = getResources().getDrawable(drawableRes);
        final Canvas canvas = new Canvas();
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    protected void injectActivity(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @NonNull
    @Override
    public ChartActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_chart;
    }

}
