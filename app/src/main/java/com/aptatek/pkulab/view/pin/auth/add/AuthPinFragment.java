package com.aptatek.pkulab.view.pin.auth.add;

import androidx.annotation.NonNull;
import android.view.View;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.data.PinCode;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.pin.auth.AuthPinHostActivity;
import com.aptatek.pkulab.view.pin.base.BasePinFragment;

import javax.inject.Inject;


public class AuthPinFragment extends BasePinFragment implements AuthPinView {

    private static final String TAG_PIN_DIALOG = "com.aptatek.pkulab.auth.pin";

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
        mainHeaderView.setTitle(getString(R.string.auth_pin_title));
        mainHeaderView.setSubtitle(getString(R.string.auth_pin_hint));
        messageTextView.setVisibility(View.INVISIBLE);
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
        showSuccess(getString(R.string.auth_pin_successful));
        fillCircle(R.drawable.pin_circle_filled_green, ((AuthPinHostActivity) getBaseActivity())::successfullyAuthorized);
    }

    private void showSuccess(@NonNull final String message) {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
        messageTextView.setBackgroundResource(R.drawable.pin_valid_message_background);
    }

    @Override
    public void onInvalidPinTyped() {
        showErrorMessage(getString(R.string.auth_pin_message_invalid));
        fillCircle(R.drawable.pin_circle_filled_red, null);
    }

    @Override
    public void showAlertDialog() {
        onInvalidPinTyped();
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.auth_pin_attempt_limit_reached_title))
                .setMessage(getString(R.string.auth_pin_attempt_limit_reached_hint))
                .setPositiveButtonText(getString(R.string.alertdialog_button_ok))
                .setCancelable(false)
                .build();

        final AlertDialogFragment dialogFragment = AlertDialogFragment.create(model, null);
        dialogFragment.show(getBaseActivity().getSupportFragmentManager(), TAG_PIN_DIALOG);
    }

    @Override
    public void onValidFingerprintDetected() {
        showSuccess(getString(R.string.auth_pin_successful));
        fillCircle(R.drawable.pin_circle_filled_green, ((AuthPinHostActivity) getBaseActivity())::successfullyAuthorized);
    }

    private void showErrorMessage(@NonNull final String message) {
        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message);
        messageTextView.setBackgroundResource(R.drawable.pin_invalid_message_background);
    }

    @Override
    public void onInvalidFingerprintDetected(@NonNull final String message) {
        showErrorMessage(message);
        fillCircle(R.drawable.pin_circle_filled_red, null);
    }

    @Override
    public void onPinButtonClicked(final View v) {
        if (messageTextView.getVisibility() == View.VISIBLE) {
            messageTextView.setVisibility(View.INVISIBLE);
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
