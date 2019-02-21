package com.aptatek.pkulab.view.connect.scan;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aptatek.pkulab.R;

public class ScanDialogItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDraw(final Canvas c, final RecyclerView parent, final RecyclerView.State state) {
        final Drawable divider = ContextCompat.getDrawable(parent.getContext(), R.drawable.reminder_settings_divider);

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            final View child = parent.getChildAt(i);

            if (divider != null) {
                final int top = child.getBottom();
                final int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(0, top, parent.getWidth(), bottom);

                divider.draw(c);
            }
        }
    }

}
