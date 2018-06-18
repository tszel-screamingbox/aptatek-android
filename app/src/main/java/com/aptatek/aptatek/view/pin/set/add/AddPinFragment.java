package com.aptatek.aptatek.view.pin.set.add;

import android.support.annotation.NonNull;
import android.view.View;

import com.aptatek.aptatek.R;
import com.aptatek.aptatek.data.PinCode;
import com.aptatek.aptatek.injection.component.FragmentComponent;
import com.aptatek.aptatek.view.pin.base.BasePinFragment;
import com.aptatek.aptatek.view.pin.set.confirm.ConfirmPinFragment;
import com.aptatek.aptatek.view.pin.set.confirm.ConfirmPinFragmentStarter;

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
        fillCircle(R.drawable.pin_circle_filled_grey, () -> {
            final ConfirmPinFragment confirmPinFragment = ConfirmPinFragmentStarter.newInstance(pinCode.getBytes());
            getBaseActivity().slideToFragment(confirmPinFragment);
        });
    }
}
