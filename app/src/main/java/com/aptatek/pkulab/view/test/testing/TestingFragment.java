package com.aptatek.pkulab.view.test.testing;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.domain.model.AlertDialogModel;
import com.aptatek.pkulab.domain.model.reader.WorkflowState;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.dialog.AlertDialogDecisions;
import com.aptatek.pkulab.view.error.ErrorModel;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.dispose.DisposeActivity;
import com.aptatek.pkulab.view.test.result.TestResultActivity;
import com.aptatek.pkulab.widget.HeaderView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestingFragment extends TestBaseFragment<TestingView, TestingPresenter> implements TestingView, LifecycleObserver {

    @Inject
    TestingPresenter presenter;

    @BindView(R.id.header)
    HeaderView headerView;

    @BindView(R.id.time_remaining)
    TextView timeRemainingView;

    @BindView(R.id.testProgress)
    ProgressBar testProgress;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    public void showTimeRemaining(TimeRemaining timeRemaining) {
        headerView.setSubtitle(null);

        timeRemainingView.setVisibility(View.VISIBLE);
        timeRemainingView.setText(timeRemaining.getTimeRemaining());
        testProgress.setVisibility(View.VISIBLE);
        testProgress.setProgress(timeRemaining.getProgressPercent());
    }

    @Override
    public void onTestFinished(final String testId) {
        requireActivity().finish();
        getBaseActivity().launchActivity(TestResultActivity.starter(requireActivity(), testId, false));
    }

    @Override
    public void onTestError(ErrorModel errorModel) {
        showAlertDialog(AlertDialogModel.builder()
                        .setTitle(errorModel.getTitle())
                        .setMessage(errorModel.getMessage())
                        .setNegativeButtonText(getString(R.string.alertdialog_button_ok))
                        .setCancelable(false)
                        .build(),
                decision -> {
                    if (decision == AlertDialogDecisions.NEGATIVE) {
                        presenter.disposeTest();
                        requireActivity().finish();
                    }
                });
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getLifecycle().addObserver(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        headerView.setSubtitle(getString(R.string.test_running_please_wait));

        timeRemainingView.setVisibility(View.GONE);
        testProgress.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        requireActivity().getLifecycle().removeObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onActivityStart() {
        presenter.onStart();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onActivityStop() {
        presenter.onStop();
    }

    @NonNull
    @Override
    public TestingPresenter createPresenter() {
        return presenter;
    }

    @Override
    public TestScreens getScreen() {
        return TestScreens.TESTING;
    }

    @Override
    public void showTurnReaderOn() {
        if (getActivity() instanceof TestActivityView) {
            ((TestActivityView) getActivity()).showTurnReaderOn();
        }
    }
}
