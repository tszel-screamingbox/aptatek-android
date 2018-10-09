package com.aptatek.pkulab.view.settings.reminder.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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

    @Override
    public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
        final int left = parent.getPaddingLeft() + parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
        final int right = parent.getWidth() - parent.getPaddingRight();
        final Drawable divider = ContextCompat.getDrawable(parent.getContext(), R.drawable.reminder_settings_divider);

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            final View child = parent.getChildAt(i);

            if (divider != null) {
                final int top = child.getBottom() + parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
                final int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);

                divider.draw(c);
            }
        }
    }
}
