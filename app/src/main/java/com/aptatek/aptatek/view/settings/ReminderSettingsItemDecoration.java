package com.aptatek.aptatek.view.settings;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.aptatek.R;

import javax.inject.Inject;

class ReminderSettingsItemDecoration extends RecyclerView.ItemDecoration {

    @Inject
    ReminderSettingsItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance);
        outRect.top = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
        outRect.bottom = parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
        int right = parent.getWidth() - parent.getPaddingRight();
        Drawable divider = ContextCompat.getDrawable(parent.getContext(), R.drawable.reminder_settings_divider);

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);

            if (divider != null) {
                int top = child.getBottom() + parent.getContext().getResources().getDimensionPixelOffset(R.dimen.general_distance_small);
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);

                divider.draw(c);
            }
        }
    }
}
