package com.aptatek.pkulab.view.test.wetting;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.AptatekApplication;
import com.aptatek.pkulab.BuildConfig;
import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTouch;

public class WettingFragment extends TestBaseFragment<WettingView, WettingPresenter> implements WettingView {

    @Inject
    WettingPresenter presenter;

    @BindView(R.id.testWettingCountdown)
    TextView tvCountdown;

    @BindView(R.id.testWettingMin)
    TextView tvCountdownMin;

    @BindView(R.id.greenDisclaimerText)
    TextView greenDisclaimer;

    private boolean isDisclaimerPressed = false;
    private boolean isAnimationPressed = false;
    private boolean incubationComplete = false;

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
        tvCountdownMin.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNextPressed() {
        if (incubationComplete) {
            stopWettingService();
            return false;
        }

        if (BuildConfig.FLAVOR.equals("prod")) {
            return true;
        }

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

    @Override
    public void setNextButtonVisible(boolean visible) {
        super.setNextButtonVisible(visible);

        if (visible) {
            incubationComplete = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setNextButtonVisible(BuildConfig.FLAVOR.equals("mock"));
        presenter.cancelCountdownNotification();
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

    public boolean warningTextTouched(final MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                isDisclaimerPressed = true;
                return true;
            case ACTION_UP:
                isDisclaimerPressed = false;
                return true;
        }
        return true;
    }

    @OnTouch(R.id.testWettingImage)
    public boolean animationTouched(final MotionEvent event) {
        if (!isDisclaimerPressed) {
            return true;
        }

        switch (event.getAction()) {
            case ACTION_DOWN:
                isAnimationPressed = true;
                return true;
            case ACTION_UP:
                isAnimationPressed = false;
                return true;
        }
        return true;
    }

    @OnTouch(R.id.header)
    public boolean headerTouched(final MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
                if (!isDisclaimerPressed || !isAnimationPressed) {
                    return true;
                }
                presenter.startEasterEggTimer();
                return true;
            case ACTION_UP:
                presenter.stopEasterEggTimer();
                return true;
        }
        return true;
    }

    @Override
    public void showWettingComplete() {
        greenDisclaimer.setVisibility(View.VISIBLE);
    }

    @Override
    public void startWettingService() {
        AptatekApplication.get(requireActivity().getApplicationContext()).startWettingServiceWhenPossible();
    }

    @Override
    public void stopWettingService() {
        AptatekApplication.get(requireActivity().getApplicationContext()).stopWettingService();
    }
}
