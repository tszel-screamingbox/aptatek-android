package com.aptatek.pkulab.view.test.wetting;

import androidx.annotation.NonNull;
import android.view.MotionEvent;
import android.widget.TextView;

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

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

public class WettingFragment extends TestBaseFragment<WettingView, WettingPresenter> implements WettingView {

    @Inject
    WettingPresenter presenter;

    @BindView(R.id.testWettingCountdown)
    TextView tvCountdown;

    private boolean isDisclaimerPressed = false;
    private boolean isAnimationPressed = false;

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
}
