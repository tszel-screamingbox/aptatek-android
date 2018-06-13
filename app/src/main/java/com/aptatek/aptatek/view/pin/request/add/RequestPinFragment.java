package com.aptatek.aptatek.view.pin.request.add;

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


public class RequestPinFragment extends BasePinFragment implements RequestPinView {

    @Inject
    RequestPinPresenter presenter;


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
        clearCircles();
        titleTextView.setText(R.string.require_pin_title);
        hintTextView.setVisibility(View.INVISIBLE);
        messageTextView.setVisibility(View.GONE);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public RequestPinPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void finishedTyping(final PinCode pinCode) {
        presenter.verifyPinCode(pinCode);
    }

    @Override
    public void onMainActivityShouldLoad() {
        final Intent intent = new Intent(getContext(), MainActivity.class);
        getBaseActivity().launchActivity(intent, true, BaseActivity.Animation.RIGHT_TO_LEFT);
    }

    @Override
    public void onInvalidPinTyped() {
        clearCircles();
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setTextColor(this.getResources().getColor(R.color.applicationRed));
        messageTextView.setText(R.string.require_pin_message_invalid);
    }

    @Override
    public void onPinButtonClicked(final View v) {
        if (messageTextView.getVisibility() == View.VISIBLE) {
            messageTextView.setVisibility(View.GONE);
        }
        super.onPinButtonClicked(v);
    }
}
