package com.aptatek.aptatek.view.pin.set.confirm;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.fingerprint.FingerprintActivity;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.pin.base.BasePinFragment;

import javax.inject.Inject;

import activitystarter.Arg;


public class ConfirmPinFragment extends BasePinFragment implements ConfirmPinView {

    @Arg
    byte[] pinBytes;

    @Inject
    ConfirmPinPresenter presenter;

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pin;
    }

    @Override
    protected void initObjects(final View view) {
        titleTextView.setText(R.string.confirm_pin_title);
        hintTextView.setText(R.string.confirm_pin_hint);
        messageTextView.setVisibility(View.GONE);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public ConfirmPinPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void finishedTyping(final PinCode pinCode) {
        presenter.verifyPin(new PinCode(pinBytes), pinCode);
    }

    @Override
    public void onMainActivityShouldLoad() {
        final Intent intent = new Intent(getContext(), MainActivity.class);
        getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onValidPinTyped() {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.confirm_pin_successful);
        messageTextView.setTextColor(getResources().getColor(R.color.applicationGreen));
        fillCircle(R.drawable.pin_circle_filled_green, presenter::navigateForward);
    }

    @Override
    public void onInvalidPinTyped() {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.confirm_pin_error);
        messageTextView.setTextColor(getResources().getColor(R.color.applicationRed));
        fillCircle(R.drawable.pin_circle_filled_red, getBaseActivity()::onBackPressed);
    }

    @Override
    public void onFingerprintActivityShouldLoad() {
        final Intent intent = new Intent(getContext(), FingerprintActivity.class);
        getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
    }
}
