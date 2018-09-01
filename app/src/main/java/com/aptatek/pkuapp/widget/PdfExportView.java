package com.aptatek.pkuapp.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.aptatek.pkuapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class PdfExportView extends ConstraintLayout {

    @BindView(R.id.textViewFastingNumber)
    TextView tv;

    public PdfExportView(final Context context) {
        super(context);
    }

    public PdfExportView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public PdfExportView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(){
        Timber.d("");
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(final boolean changed, final int l, final int t, final int r, final int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        final int measureWidth =
                View.MeasureSpec.makeMeasureSpec(getContext().getResources().getDimensionPixelSize(R.dimen.pdf_width), View.MeasureSpec.EXACTLY);
        final int measuredHeight =
                View.MeasureSpec.makeMeasureSpec(getContext().getResources().getDimensionPixelSize(R.dimen.pdf_height), View.MeasureSpec.EXACTLY);

        measure(measureWidth, measuredHeight);

        layout(0, 0, measureWidth * 2, measuredHeight * 2);
    }
}
