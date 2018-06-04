package com.aptatek.aptatek.view.pin.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.component.ActivityComponent;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.pin.BasePinActivity;

import javax.inject.Inject;

public class SetPinActivity extends BasePinActivity implements SetPinActivityView {

    @Inject
    SetPinActivityPresenter presenter;


    @Override
    protected void finishedTyping(PinCode pinCode) {
        presenter.setPinCode(pinCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void injectActivity(ActivityComponent activityComponent) {
        getActivityComponent().inject(this);
    }

    @NonNull
    @Override
    public SetPinActivityPresenter createPresenter() {
        return presenter;
    }

    @Override
    public int getFrameLayoutId() {
        return R.layout.activity_pin;
    }


    @Override
    public void onValidFingerPrintTyped() {
        fillCircle(PIN_LENGTH, R.drawable.pin_circle_filled_green);
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.set_pin_set_message);
        messageTextView.setTextColor(getResources().getColor(R.color.applicationGreen));
    }

    @Override
    public void onInvalidFingerPrintTyped() {
        fillCircle(PIN_LENGTH, R.drawable.pin_circle_filled_red);
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.set_pin_confirm_error);
        messageTextView.setTextColor(getResources().getColor(R.color.applicationRed));
    }

    @Override
    public void onShowConfirmationTexts() {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
        findViewById(android.R.id.content).startAnimation(animation);
        clearCircles();
        titleTextView.setText(R.string.set_pin_confirm_title);
        hintTextView.setText(R.string.set_pin_confirm_hint);
        messageTextView.setVisibility(View.GONE);
    }

    @Override
    public void onMainActivityShouldLoad() {
        Intent intent = new Intent(this, MainActivity.class);
        launchActivity(intent, true, Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onResetPinActivityShouldLoad() {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        findViewById(android.R.id.content).startAnimation(animation);
        clearCircles();
        titleTextView.setText(R.string.set_pin_set_title);
        hintTextView.setText(R.string.set_pin_set_hint);
        messageTextView.setVisibility(View.GONE);
    }
}


