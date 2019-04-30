package com.aptatek.pkulab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.aptatek.pkulab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderView extends ConstraintLayout {

    @BindView(R.id.title)
    TextView titleTextView;
    @BindView(R.id.subtitle)
    TextView subtitleTextView;

    public HeaderView(final Context context) {
        this(context, null, 0);
    }

    public HeaderView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(getContext()).inflate(R.layout.layout_header_view, this);

        ButterKnife.bind(this);

        final TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.HeaderView, 0, 0);

        try {
            final String title = array.getString(R.styleable.HeaderView_titleText);
            final String subTitle = array.getString(R.styleable.HeaderView_subTitleText);
            titleTextView.setText(title);
            subtitleTextView.setText(subTitle);
        } finally {
            array.recycle();
        }
    }

    public TextView getSubtitleTextView() {
        return subtitleTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setSubtitle(final String subtitle) {
        subtitleTextView.setText(subtitle);
        invalidate();
    }

    public void setTitle(final String title) {
        titleTextView.setText(title);
        invalidate();
    }
}
