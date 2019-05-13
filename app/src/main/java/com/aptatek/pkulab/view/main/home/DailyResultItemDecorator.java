package com.aptatek.pkulab.view.main.home;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;

import javax.inject.Inject;

class DailyResultItemDecorator extends RecyclerView.ItemDecoration {

    @Inject
    DailyResultItemDecorator() {
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        outRect.bottom = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_mini);
    }
}
