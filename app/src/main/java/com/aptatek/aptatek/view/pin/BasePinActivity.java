package com.aptatek.aptatek.view.pin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.base.IActivityComponentProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BasePinActivity extends BaseActivity implements IActivityComponentProvider {

    private static final int PIN_LENGTH = 6;

    private String pin = "";

    @BindView(R.id.pinLayout)
    protected LinearLayout pinCircleLinearLayout;


    protected abstract void finishedTyping(PinCode pinCode);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectActivity(activityComponent());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        ButterKnife.bind(this);
    }


    protected void clearCircles() {
        for (int i = 0; i < pinCircleLinearLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) pinCircleLinearLayout.getChildAt(i);
            imageView.setImageResource(R.drawable.pin_circle);
        }
    }

    private void fillCircle(int untilAt) {
        clearCircles();
        for (int i = 0; i < untilAt; i++) {
            ImageView imageView = (ImageView) pinCircleLinearLayout.getChildAt(i);
            imageView.setImageResource(R.drawable.pin_circle_filled);
        }
    }


    @OnClick({R.id.button0, R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8,
            R.id.button9})
    public void onPinButtonClicked(View v) {
        Button button = findViewById(v.getId());
        pin = pin + button.getText().toString();
        fillCircle(pin.length());

        if (pin.length() == PIN_LENGTH) {
            PinCode pinCode = new PinCode(pin.getBytes());
            finishedTyping(pinCode);
            pin = "";
        }
    }

    @OnClick(R.id.buttonDelete)
    public void onDeleteButtonClicked() {
        if (pin.length() > 0) {
            pin = pin.substring(0, pin.length() - 1);
            fillCircle(pin.length());
        }
    }
}