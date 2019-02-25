package com.aptatek.pkulab.view.test.turnreaderon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.injection.component.FragmentComponent;
import com.aptatek.pkulab.view.connect.turnreaderon.TurnReaderOnFragment;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisionListener;
import com.aptatek.pkulab.view.test.TestActivityCommonView;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.turnreaderon.permission.PermissionRequiredOnTestActivity;

import javax.inject.Inject;

public class TurnReaderOnTestFragment extends TurnReaderOnFragment<TurnReaderOnTestView, TurnReaderOnTestPresenter> implements TurnReaderOnTestView {

    private static final String KEY_NEXT_SCREEN = "pkulab.test.nextscreen";

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
    public void showAlertDialog(@NonNull final AlertDialogModel alertDialogModel, @Nullable final AlertDialogDecisionListener listener) {
        // do nothing here
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

    private void runOnTestTestActivityView(final TestActivityViewAction action) {
        if (getActivity() instanceof TestActivityCommonView) {
            action.run((TestActivityCommonView) getActivity());
        }
    }

    private interface TestActivityViewAction {
        void run(TestActivityCommonView view);
    }

}
