package com.aptatek.pkulab.widget;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.aptatek.pkulab.R;

public class NumberPicker extends android.widget.NumberPicker {

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextColor(ContextCompat.getColor(getContext(), R.color.applicationBlack));
            ((EditText) view).setTypeface(ResourcesCompat.getFont(getContext(), R.font.nunito_black));
        }
    }
}