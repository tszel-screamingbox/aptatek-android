package com.aptatek.pkulab.view.pin.set.add;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.pin.base.BasePinFragment;
import com.aptatek.pkulab.view.pin.set.confirm.ConfirmPinFragment;
import com.aptatek.pkulab.view.pin.set.confirm.ConfirmPinFragmentStarter;

import javax.inject.Inject;


public class AddPinFragment extends BasePinFragment implements AddPinView {

    @Inject
    AddPinPresenter presenter;


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
        titleTextView.setText(R.string.set_pin_title);
        hintTextView.setText(R.string.set_pin_hint);
        messageTextView.setVisibility(View.GONE);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @NonNull
    @Override
    public AddPinPresenter createPresenter() {
        return presenter;
    }

    @Override
    protected void finishedTyping(final PinCode pinCode) {
        presenter.finishedTyping(pinCode);
    }

    @Override
    public void forward(final byte[] pinBytes) {
        fillCircle(R.drawable.pin_circle_filled_grey, () -> {
            final ConfirmPinFragment confirmPinFragment = ConfirmPinFragmentStarter.newInstance(pinBytes);
            getBaseActivity().slideToFragment(confirmPinFragment);
        });
    }

    @Override
    public void invalidPinFormat(final String message) {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(R.string.confirm_pin_error);
        messageTextView.setBackgroundResource(R.drawable.pin_invalid_message_background);
        fillCircle(R.drawable.pin_circle_filled_red, () -> {
        });
    }
}
