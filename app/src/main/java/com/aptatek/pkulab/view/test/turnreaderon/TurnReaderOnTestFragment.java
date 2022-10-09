package com.aptatek.pkulab.view.test.turnreaderon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.dialog.AlertDialogFragment;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;
import com.aptatek.pkulab.view.test.turnreaderon.permission.PermissionRequiredOnTestActivity;

import javax.inject.Inject;

public class TurnReaderOnTestFragment extends TurnReaderOnFragment<TurnReaderOnTestView, TurnReaderOnTestPresenter> implements TurnReaderOnTestView, LifecycleObserver {

    private static final String KEY_NEXT_SCREEN = "pkulab.test.nextscreen";
    private static final String TAG_ALERT = "pkulab.test.alert";

    public static TurnReaderOnTestFragment create(@NonNull final TestScreens navigateHereAfterConnection) {
        final Bundle args = new Bundle();
        args.putInt(KEY_NEXT_SCREEN, navigateHereAfterConnection.ordinal());

        final TurnReaderOnTestFragment fragment = new TurnReaderOnTestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Inject
    TurnReaderOnTestPresenter presenter;

    @Override
    protected void initObjects(final View view) {
        super.initObjects(view);
        presenter.initWithDefaults();

        requireActivity().getLifecycle().addObserver(this);
    }

    @Override
    public void onDestroyView() {
        requireActivity().getLifecycle().removeObserver(this);

        super.onDestroyView();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onForeground() {
        presenter.onResumed();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onBackground() {
        presenter.onPaused();
    }

    @Override
    public void playVideo(@NonNull final Uri uri, final boolean shouldLoop) {
        super.playVideo(uri, shouldLoop);
    }

    @Override
    protected void injectFragment(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public void displaySelfCheckAnimation() {
        super.displaySelfCheckAnimation();
        presenter.getBatteryLevel();
    }

    @Override
    public void onSelfCheckComplete() {
        if (getArguments() == null) {
            runOnTestTestActivityView(TestActivityCommonView::showNextScreen);
        } else {
            final int testScreenOrdinal = getArguments().getInt(KEY_NEXT_SCREEN);
            final TestScreens nextScreen = TestScreens.values()[testScreenOrdinal];
            if (getActivity() instanceof TestActivityView) {
                ((TestActivityView) getActivity()).showScreen(nextScreen);
            }
        }
    }

    @Override
    public void displayMissingPermissions() {
        getBaseActivity().launchActivity(new Intent(requireActivity(), PermissionRequiredOnTestActivity.class));
    }

    @Override
    public boolean onNextPressed() {
        return false;
    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void showPreviousScreen() {

    }

    @NonNull
    @Override
    public TurnReaderOnTestPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void setMessage(@NonNull final String message) {
        // do nothing here
    }

    @Override
    public void setMessageHtml(@NonNull Spanned spannable) {
        // do nothing here
    }

    @Override
    public void showAlertDialog(@NonNull final AlertDialogModel alertDialogModel, @Nullable final AlertDialogDecisionListener listener) {
        if (getChildFragmentManager().findFragmentByTag(TAG_ALERT) == null) {
            final AlertDialogFragment alertDialogFragment = AlertDialogFragment.create(alertDialogModel, listener);
            alertDialogFragment.show(getChildFragmentManager(), TAG_ALERT);
        }
    }

    @Override
    public void setTitle(@NonNull final String title) {
        // do nothing here
    }

    @Override
    public void setBottomBarVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setBottomBarVisible(visible));
    }

    @Override
    public void setBatteryIndicatorVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setBatteryIndicatorVisible(visible));
    }

    @Override
    public void setBatteryPercentage(final int percentage) {
        runOnTestTestActivityView(view -> view.setBatteryPercentage(percentage));
    }

    @Override
    public void setProgressVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setProgressVisible(visible));
    }

    @Override
    public void setProgressPercentage(final int percentage) {
        // do nothing here
    }

    @Override
    public void setDisclaimerViewVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setDisclaimerViewVisible(visible));
    }

    @Override
    public void setDisclaimerMessage(@NonNull final String message) {
        // do nothing here
    }

    @Override
    public void setNextButtonVisible(final boolean visible) {
        runOnTestTestActivityView(view -> view.setNextButtonVisible(visible));
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.TURN_READER_ON;
    }

    @Override
    public void showTestingScreen() {
        runOnTestTestActivityView(view -> view.showScreen(TestScreens.TESTING));
    }

    @Override
    public void showConnectItAllScreen() {
        runOnTestTestActivityView(view -> view.showScreen(TestScreens.CONNECT_IT_ALL));
    }

    @Override
    public void showTestResultScreen(final String testId) {
        final FragmentActivity activity = requireActivity();
        startActivity(TestResultActivity.starter(activity, testId, false));
        activity.finish();
    }

    private void runOnTestTestActivityView(final TestActivityViewAction action) {
        if (getActivity() instanceof TestActivityView) {
            action.run((TestActivityView) getActivity());
        }
    }

    private interface TestActivityViewAction {
        void run(TestActivityView view);
    }

    @Override
    public void showErrorScreen(ErrorModel errorModel) {
        showAlertDialog(AlertDialogModel.builder()
                        .setErrorCode(errorModel.getErrorCode())
                        .setAlertHeader(true)
                        .setTitle(errorModel.getTitle())
                        .setMessage(errorModel.getMessage())
                        .setNegativeButtonText(getString(R.string.alertdialog_button_ok))
                        .setCancelable(false)
                        .build(),
                decision -> {
                    if (decision == AlertDialogDecisions.NEGATIVE) {
                        if (errorModel.isAfterChamberScrewedOn()) {
                            requireActivity().finish();
                            getBaseActivity().launchActivity(new Intent(getActivity(), DisposeActivity.class));
                        } else {
                            presenter.disposeTest();
                            runOnTestTestActivityView(TestActivityView::onBackPressed);
                        }
                    }
                });
    }
}
