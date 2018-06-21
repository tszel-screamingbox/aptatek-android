package com.aptatek.aptatek.view.settings;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.aptatek.R;

import javax.inject.Inject;

class ReminderItemDecoration extends RecyclerView.ItemDecoration {

    @Inject
    ReminderItemDecoration() {
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        outRect.top = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_xmini);
    }
}
