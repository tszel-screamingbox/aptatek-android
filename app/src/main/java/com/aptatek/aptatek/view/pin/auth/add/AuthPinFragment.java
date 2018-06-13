package com.aptatek.aptatek.view.pin.auth.add;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.base.BaseActivity;
import com.aptatek.aptatek.view.main.MainActivity;
import com.aptatek.aptatek.view.pin.base.BasePinFragment;

import javax.inject.Inject;


public class AuthPinFragment extends BasePinFragment implements AuthPinView {

    @Inject
    AuthPinPresenter presenter;


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
        presenter.initView();
        titleTextView.setText(R.string.require_pin_title);
        messageTextView.setVisibility(View.GONE);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public AuthPinPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void finishedTyping(final PinCode pinCode) {
        presenter.verifyPinCode(pinCode);
    }

    @Override
    public void onValidPinTyped() {
        messageTextView.setVisibility(View.GONE);
        fillCircle(R.drawable.pin_circle_filled_green, () -> {
            final Intent intent = new Intent(getContext(), MainActivity.class);
            getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
        });
    }

    @Override
    public void onInvalidPinTyped() {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setTextColor(this.getResources().getColor(R.color.applicationRed));
        messageTextView.setText(R.string.require_pin_message_invalid);
        fillCircle(R.drawable.pin_circle_filled_red, null);
    }

    @Override
    public void onValidFingerprintDetected() {
        messageTextView.setVisibility(View.GONE);
        fillCircle(R.drawable.pin_circle_filled_green, () -> {
            final Intent intent = new Intent(getContext(), MainActivity.class);
            getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
        });
    }

    @Override
    public void onInvalidFingerprintDetected(@NonNull String message) {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setTextColor(this.getResources().getColor(R.color.applicationRed));
        messageTextView.setText(message);
        fillCircle(R.drawable.pin_circle_filled_red, null);
    }

    @Override
    public void onFingerprintAvailable() {
        fingerprintImageView.setVisibility(View.VISIBLE);
        hintTextView.setText(R.string.require_pin_hint_fingerprint);
    }

    @Override
    public void onFingerprintDisabled() {
        fingerprintImageView.setVisibility(View.GONE);
        hintTextView.setText(R.string.require_pin_hint);
    }

    @Override
    public void onPinButtonClicked(final View v) {
        if (messageTextView.getVisibility() == View.VISIBLE) {
            messageTextView.setVisibility(View.GONE);
        }
        super.onPinButtonClicked(v);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.stopListening();
    }
}
