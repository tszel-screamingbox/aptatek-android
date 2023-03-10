package com.aptatek.pkulab.view.pin.set.confirm;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.base.BaseActivity;
import com.aptatek.pkulab.view.connect.onboarding.ConnectOnboardingReaderActivity;
import com.aptatek.pkulab.view.fingerprint.FingerprintActivity;
import com.aptatek.pkulab.view.pin.base.BasePinFragment;
import com.aptatek.pkulab.view.pin.set.SetPinHostActivity;

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
        mainHeaderView.setTitle(getString(R.string.confirm_pin_title));
        mainHeaderView.setSubtitle(getString(R.string.confirm_pin_hint));
        messageTextView.setVisibility(View.INVISIBLE);
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
    public void onConnectReaderShouldLoad() {
        getBaseActivity().launchActivity(ConnectOnboardingReaderActivity.starter(getActivity()), true, BaseActivity.Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onValidPinTyped() {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.confirm_pin_successful);
        messageTextView.setBackgroundResource(R.drawable.pin_valid_message_background);
        innerFillCircle(PIN_LENGTH, R.drawable.pin_circle_filled_green);

        if (getActivity() instanceof SetPinHostActivity) {
            ((SetPinHostActivity) getActivity()).onValidPinTyped();
        }
    }

    @Override
    public void onInvalidPinTyped() {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.confirm_pin_error);
        messageTextView.setBackgroundResource(R.drawable.pin_invalid_message_background);
        fillCircle(R.drawable.pin_circle_filled_red, getBaseActivity()::onBackPressed);

        if (getActivity() instanceof SetPinHostActivity) {
            ((SetPinHostActivity) getActivity()).onInvalidPinTyped();
        }
    }

    @Override
    public void onFingerprintActivityShouldLoad() {
        final Intent intent = new Intent(getContext(), FingerprintActivity.class);
        getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
    }
}
