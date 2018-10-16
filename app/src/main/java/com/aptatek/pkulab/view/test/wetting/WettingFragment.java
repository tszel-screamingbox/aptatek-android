package com.aptatek.pkulab.view.test.wetting;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class WettingFragment extends TestBaseFragment<WettingView, WettingPresenter> implements WettingView {

    @Inject
    WettingPresenter presenter;

    @BindView(R.id.testWettingCountdown)
    TextView tvCountdown;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_wetting;
    }

    @Override
    public void showCountdown(@NonNull final String countdownRemaining) {
        tvCountdown.setText(countdownRemaining);
    }

    @Override
    public void onCountdownFinished() {
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).showNextScreen();
        }
    }

    @Override
    public boolean onNextPressed() {
        final AlertDialogModel model = AlertDialogModel.builder()
                .setTitle(getString(R.string.test_wetting_alert_title))
                .setMessage(getString(R.string.test_wetting_alert_message))
                .setPositiveButtonText(getString(R.string.alertdialog_button_yes))
                .setNegativeButtonText(getString(R.string.alertdialog_button_no))
                .setCancelable(false)
                .build();

        showAlertDialog(model, decision -> {
            if (decision == AlertDialogDecisions.POSITIVE) {
                presenter.resetWetting();
            }
        });

        return true;
    }

    @NonNull
    @Override
    public WettingPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.WETTING;
    }
}
