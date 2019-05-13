package com.aptatek.pkulab.view.settings.reminder.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.aptatek.pkulab.R;

import javax.inject.Inject;

public class ReminderSettingsItemDecoration extends RecyclerView.ItemDecoration {

    @Inject
    ReminderSettingsItemDecoration() {
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent, final RecyclerView.State state) {
        outRect.left = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance);
        outRect.top = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
        outRect.bottom = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
    }
}
