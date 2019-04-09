package com.aptatek.pkulab.view.test.testing;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.aptatek.pkulab.R;
import com.aptatek.pkulab.injection.component.test.TestFragmentComponent;
import com.aptatek.pkulab.view.test.TestActivityView;
import com.aptatek.pkulab.view.test.TestScreens;
import com.aptatek.pkulab.view.test.base.TestBaseFragment;
import com.aptatek.pkulab.view.test.result.TestResultActivity;

import javax.inject.Inject;

public class TestingFragment extends TestBaseFragment<TestingView, TestingPresenter> implements TestingView, LifecycleObserver {

    @Inject
    TestingPresenter presenter;

    @Override
    protected void injectTestFragment(final @NonNull TestFragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
    }

    @Override
    public void onTestFinished(final String testId) {
        requireActivity().finish();
        getBaseActivity().launchActivity(TestResultActivity.starter(requireActivity(), testId));
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getLifecycle().addObserver(this);
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
