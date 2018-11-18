package com.aptatek.pkulab.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

public class CustomRadioGroup extends ConstraintLayout implements OnClickListener {

    private RadioButton activeRadioButton;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if (activeRadioButton != null) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = rb;
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        setChildrenOnClickListener();
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        setChildrenOnClickListener();
    }


    private void setChildrenOnClickListener() {
        final int c = getChildCount();
        for (int i = 0; i < c; i++) {
            final View v = getChildAt(i);
            if (v instanceof RadioButton) {
                v.setOnClickListener(this);
                if (((RadioButton) v).isChecked()) {
                    check(v.getId());
                }
            }
        }
    }

    public void check(@IdRes int id) {

        if (id != -1 && (id == getCheckedRadioButtonId())) {
            return;
        }
        if (getCheckedRadioButtonId() != -1) {
            setCheckedStateForView(getCheckedRadioButtonId(), false);
        }
        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        activeRadioButton = findViewById(id);
    }

    public int getCheckedRadioButtonId() {
        if (activeRadioButton != null) {
            return activeRadioButton.getId();
        }

        return -1;
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }
}
