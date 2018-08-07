package com.aptatek.pkuapp.view.settings.reminder.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.pkuapp.R;

import javax.inject.Inject;

class ReminderItemDecoration extends RecyclerView.ItemDecoration {

    @Inject
    ReminderItemDecoration() {
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        outRect.top = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_xmini);
    }
}
