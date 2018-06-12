package com.aptatek.aptatek.view.pin.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.view.base.BaseFragment;

import activitystarter.ActivityStarter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BasePinFragment extends BaseFragment {

    protected static final int PIN_LENGTH = 6;

    private String pin = "";

    @BindView(R.id.pinLayout)
    protected ConstraintLayout pinCircleConstrainLayout;

    @BindView(R.id.title)
    protected TextView titleTextView;

    @BindView(R.id.hintTextView)
    protected TextView hintTextView;

    @BindView(R.id.messageTextView)
    protected TextView messageTextView;

    protected abstract void finishedTyping(PinCode pinCode);

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        ActivityStarter.fill(this, savedInstanceState);
        return view;
    }


    protected void clearCircles() {
        for (int i = 0; i < pinCircleConstrainLayout.getChildCount(); i++) {
            final ImageView imageView = (ImageView) pinCircleConstrainLayout.getChildAt(i);
            imageView.setImageResource(R.drawable.pin_circle);
        }
    }

    protected void fillCircle(final int untilAt, final int resId) {
        clearCircles();
        for (int i = 0; i < untilAt; i++) {
            final ImageView imageView = (ImageView) pinCircleConstrainLayout.getChildAt(i);
            imageView.setImageResource(resId);
        }
    }


    @OnClick({R.id.button0, R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8,
            R.id.button9})
    public void onPinButtonClicked(final View v) {
        final Button button = v.findViewById(v.getId());
        pin = pin + button.getText().toString();
        fillCircle(pin.length(), R.drawable.pin_circle_filled_grey);

        if (pin.length() == PIN_LENGTH) {
            final PinCode pinCode = new PinCode(pin.getBytes());
            finishedTyping(pinCode);
            pin = "";
        }
    }

    @OnClick(R.id.buttonDelete)
    public void onDeleteButtonClicked() {
        if (pin.length() > 0) {
            pin = pin.substring(0, pin.length() - 1);
            fillCircle(pin.length(), R.drawable.pin_circle_filled_grey);
        }
    }
}
